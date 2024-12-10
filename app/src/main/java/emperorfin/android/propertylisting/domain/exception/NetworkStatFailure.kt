package emperorfin.android.propertylisting.domain.exception

import androidx.annotation.StringRes
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.domain.exception.Failure.FeatureFailure


sealed class NetworkStatFailure(
    @StringRes open val message: Int,
    open val cause: Throwable?
) : FeatureFailure() {

    class NetworkStatListNotAvailableMemoryError(
        @StringRes override val message: Int = R.string.error_memory_network_stat_list_not_available,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NetworkStatListNotAvailableLocalError(
        @StringRes override val message: Int = R.string.error_local_network_stat_list_not_available,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NetworkStatListNotAvailableRemoteError(
        @StringRes override val message: Int = R.string.error_remote_network_stat_list_not_available,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NetworkStatMemoryError(
        @StringRes override val message: Int = R.string.error_memory_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NetworkStatLocalError(
        @StringRes override val message: Int = R.string.error_local_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NetworkStatRemoteError(
        @StringRes override val message: Int = R.string.error_remote_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class GetNetworkStatMemoryError(
        @StringRes override val message: Int = R.string.error_memory_get_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class GetNetworkStatLocalError(
        @StringRes override val message: Int = R.string.error_local_get_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class GetNetworkStatRemoteError(
        @StringRes override val message: Int = R.string.error_remote_get_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class InsertNetworkStatMemoryError(
        @StringRes override val message: Int = R.string.error_memory_insert_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class InsertNetworkStatLocalError(
        @StringRes override val message: Int = R.string.error_local_insert_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class InsertNetworkStatRemoteError(
        @StringRes override val message: Int = R.string.error_remote_insert_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class UpdateNetworkStatMemoryError(
        @StringRes override val message: Int = R.string.error_memory_update_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class UpdateNetworkStatLocalError(
        @StringRes override val message: Int = R.string.error_local_update_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class UpdateNetworkStatRemoteError(
        @StringRes override val message: Int = R.string.error_remote_update_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class DeleteNetworkStatMemoryError(
        @StringRes override val message: Int = R.string.error_memory_delete_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class DeleteNetworkStatLocalError(
        @StringRes override val message: Int = R.string.error_local_delete_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class DeleteNetworkStatRemoteError(
        @StringRes override val message: Int = R.string.error_remote_delete_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NonExistentNetworkStatDataMemoryError(
        @StringRes override val message: Int = R.string.error_memory_non_existent_network_stat_data,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NonExistentNetworkStatDataLocalError(
        @StringRes override val message: Int = R.string.error_local_non_existent_network_stat_data,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NonExistentNetworkStatDataRemoteError(
        @StringRes override val message: Int = R.string.error_remote_non_existent_network_stat_data,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    // For Repositories
    class GetNetworkStatRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_get_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class InsertNetworkStatRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_insert_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class DeleteNetworkStatRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_delete_network_stat,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)

    class NetworkStatListNotAvailableRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_network_stat_list_not_available,
        override val cause: Throwable? = null
    ) : NetworkStatFailure(message = message, cause = cause)
}