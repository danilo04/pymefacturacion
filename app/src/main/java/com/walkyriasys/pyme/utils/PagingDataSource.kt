package com.walkyriasys.pyme.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PagingDataSource<PARAMS : Any, DOMAIN_MODEL, UI_MODEL>(
    val viewModelScope: CoroutineScope,
    val backgroundDispatcher: CoroutineDispatcher,
    val loadPage: (PARAMS, page: Int) -> Flow<List<DOMAIN_MODEL>>,
    val builder: (
        pages: Flow<Pages<DOMAIN_MODEL, PARAMS>>
    ) -> Flow<UI_MODEL>,
    val liveParams: Flow<PARAMS>? = null,
) {
    // Input parameters
    private lateinit var params: PARAMS

    // True once the `start` method has been called
    private var started = false

    // Cached live loaded pages
    private val _pages = MutableStateFlow<Map<Int, Page<DOMAIN_MODEL>>>(emptyMap())

    // All the running jobs
    private val jobs = mutableMapOf<Int, Job>()

    init {
        start()
    }

    /**
     * Consume this UI state by your UI
     */
    val uiState = builder(_pages.filter { it.isNotEmpty() }.map { pageMap ->
        val pages = pageMap.toList().sortedBy { (page, _) -> page }
        val lastPage = pages.lastOrNull()
        val lastPageIndex = lastPage?.first
        val loadNextPage = lastPage == null || lastPage.second.data.isNotEmpty()
        Pages(lastPageIndex, params, pages.flatMap { it.second.data }) {
            if (loadNextPage) {
                val nextPage = (lastPageIndex ?: -1) + 1
                load(nextPage)
            }
        }
    }).distinctUntilChanged().flowOn(backgroundDispatcher)

    /**
     * Starts loading the initial page with the given parameters. It stops all the previous live
     * loading jobs (which were triggered with different parameters).
     */
    fun start(params: PARAMS) {
        viewModelScope.launch {
            if (started && params != this@PagingDataSource.params) {
                jobs.values.forEach { it.cancel() }
                jobs.clear()
                _pages.value = emptyMap()
            }
            this@PagingDataSource.params = params
            started = true
            load(0)
        }
    }

    /**
     * Starts loading the initial page from the input flow of parameters if the flow is set
     */
    private fun start() {
        if (liveParams != null) {
            viewModelScope.launch {
                liveParams.collect { params ->
                    start(params)
                }
            }
        }
    }

    /**
     * Loads the given page the first time unless it's been already loaded. It's collecting data
     * changes from the database and updating the `_pages` flow with the new data.
     */
    private fun load(page: Int) {
        if (_pages.value[page] == null) {
            jobs[page]?.cancel()
            jobs[page] = loadPage(params, page).distinctUntilChanged().onEach { domainModels ->
                val mutableMap = _pages.value.toMutableMap()
                mutableMap[page] = Page(domainModels)
                _pages.value = mutableMap
            }.launchIn(viewModelScope)
        }
    }

    /**
     * This function shouldn't be necessary once we're correctly using Room methods to update the
     * database everywhere. Right now it's triggered by the fragment when it knows a certain item
     * should be updated. It will call the load method again for the page that matches the
     * predicate.
     */
    fun refresh(predicate: (DOMAIN_MODEL) -> Boolean) {
        val pageObject = _pages.value.toList()
            .find { (_, value) -> value.data.any { dataItem -> predicate(dataItem) } }
            ?: return
        val page = pageObject.first

        jobs[page]?.cancel()
        jobs[page] = loadPage(params, page).onEach { domainModels ->
            val mutableMap = _pages.value.toMutableMap()
            mutableMap[page] = Page(domainModels)
            _pages.value = mutableMap
        }.launchIn(viewModelScope)
    }

    /**
     * Model representing a page of data for given params.
     */
    data class Page<DOMAIN_MODEL>(
        val data: List<DOMAIN_MODEL>
    )

    data class Pages<DOMAIN_MODEL, PARAMS>(
        val lastPage: Int?,
        val params: PARAMS,
        val data: List<DOMAIN_MODEL>,
        val loadMore: () -> Unit
    )
}

