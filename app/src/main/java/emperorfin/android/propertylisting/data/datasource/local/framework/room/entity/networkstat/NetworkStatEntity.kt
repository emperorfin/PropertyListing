package emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat

import androidx.room.ColumnInfo
import androidx.room.Entity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity.Companion.COLUMN_INFO_REQUEST_METHOD
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity.Companion.TABLE_NAME
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatEntityParams



// The Room ORM won't be able to instantiate this class if it's one and only constructor is made
// private.
@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [COLUMN_INFO_REQUEST_METHOD]
)
data class NetworkStatEntity(
    @ColumnInfo(name = COLUMN_INFO_REQUEST_METHOD)
    override val requestMethod: String,
    @ColumnInfo(name = COLUMN_INFO_DURATION)
    override val duration: Long
) : NetworkStatEntityParams {

    companion object {

        const val TABLE_NAME = "table_network_stats"

        const val COLUMN_INFO_REQUEST_METHOD = "request_method"
        const val COLUMN_INFO_DURATION = "duration"

        fun newInstance(requestMethod: String, duration: Long): NetworkStatEntity {
            return NetworkStatEntity(
                requestMethod = requestMethod,
                duration = duration,
            )
        }

    }

}
