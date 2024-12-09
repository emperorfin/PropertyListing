package emperorfin.android.propertylisting.domain.exception

import androidx.annotation.StringRes
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.domain.exception.Failure.FeatureFailure

sealed class PropertyFailure(
    @StringRes open val message: Int,
    open val cause: Throwable?
) : FeatureFailure() {

    class PropertyListNotAvailableMemoryError(
        @StringRes override val message: Int = R.string.error_memory_property_list_not_available,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class PropertyListNotAvailableLocalError(
        @StringRes override val message: Int = R.string.error_local_property_list_not_available,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class PropertyListNotAvailableRemoteError(
        @StringRes override val message: Int = R.string.error_remote_property_list_not_available,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class PropertyMemoryError(
        @StringRes override val message: Int = R.string.error_memory_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class PropertyLocalError(
        @StringRes override val message: Int = R.string.error_local_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class PropertyRemoteError(
        @StringRes override val message: Int = R.string.error_remote_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class GetPropertyMemoryError(
        @StringRes override val message: Int = R.string.error_memory_get_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class GetPropertyLocalError(
        @StringRes override val message: Int = R.string.error_local_get_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class GetPropertyRemoteError(
        @StringRes override val message: Int = R.string.error_remote_get_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class InsertPropertyMemoryError(
        @StringRes override val message: Int = R.string.error_memory_insert_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class InsertPropertyLocalError(
        @StringRes override val message: Int = R.string.error_local_insert_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class InsertPropertyRemoteError(
        @StringRes override val message: Int = R.string.error_remote_insert_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class UpdatePropertyMemoryError(
        @StringRes override val message: Int = R.string.error_memory_update_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class UpdatePropertyLocalError(
        @StringRes override val message: Int = R.string.error_local_update_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class UpdatePropertyRemoteError(
        @StringRes override val message: Int = R.string.error_remote_update_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class DeletePropertyMemoryError(
        @StringRes override val message: Int = R.string.error_memory_delete_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class DeletePropertyLocalError(
        @StringRes override val message: Int = R.string.error_local_delete_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class DeletePropertyRemoteError(
        @StringRes override val message: Int = R.string.error_remote_delete_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class NonExistentPropertyDataMemoryError(
        @StringRes override val message: Int = R.string.error_memory_non_existent_property_data,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class NonExistentPropertyDataLocalError(
        @StringRes override val message: Int = R.string.error_local_non_existent_property_data,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class NonExistentPropertyDataRemoteError(
        @StringRes override val message: Int = R.string.error_remote_non_existent_property_data,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    // For Repositories
    class GetPropertyRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_get_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class InsertPropertyRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_insert_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class DeletePropertyRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_delete_property,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)

    class PropertyListNotAvailableRepositoryError(
        @StringRes override val message: Int = R.string.error_repository_property_list_not_available,
        override val cause: Throwable? = null
    ) : PropertyFailure(message = message, cause = cause)
}