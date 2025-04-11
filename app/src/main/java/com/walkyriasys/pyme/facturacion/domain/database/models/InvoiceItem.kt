import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import com.walkyriasys.pyme.facturacion.domain.database.models.Invoice
import com.walkyriasys.pyme.facturacion.domain.database.models.Product

@Entity(
    tableName = InvoiceItem.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Invoice::class,
            parentColumns = [Invoice.ID],
            childColumns = [InvoiceItem.INVOICE_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = [Product.ID],
            childColumns = [InvoiceItem.PRODUCT_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [InvoiceItem.INVOICE_ID]),
        Index(value = [InvoiceItem.PRODUCT_ID]),
        Index(value = [InvoiceItem.UUID], unique = true), // Unique index for uuid
    ]
)
data class InvoiceItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int,
    @ColumnInfo(name = UUID)
    val uuid: String,
    @ColumnInfo(name = INVOICE_ID)
    val invoiceId: Int,
    @ColumnInfo(name = PRODUCT_ID)
    val productId: Int,
    @ColumnInfo(name = QUANTITY)
    val quantity: Int,
    @ColumnInfo(name = PRICE)
    val price: Int, // cents of currency
    @ColumnInfo(name = DISCOUNT)
    val discount: Int? // cents of currency
) {
    companion object {
        const val TABLE_NAME = "invoice_items"
        const val UUID = "uuid"
        const val INVOICE_ID = "invoice_id"
        const val PRODUCT_ID = "product_id"
        const val QUANTITY = "quantity"
        const val PRICE = "price"
        const val DISCOUNT = "discount"
        const val ID = "id"
    }
}