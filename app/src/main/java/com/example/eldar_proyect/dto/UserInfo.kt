import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val name: String,
    val surname: String,
    val cardNumber: String
) : Parcelable