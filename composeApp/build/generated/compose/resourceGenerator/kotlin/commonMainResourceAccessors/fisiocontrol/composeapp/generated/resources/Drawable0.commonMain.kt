@file:OptIn(InternalResourceApi::class)

package fisiocontrol.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/fisiocontrol.composeapp.generated.resources/"

internal val Res.drawable.icon: DrawableResource by lazy {
      DrawableResource("drawable:icon", setOf(
        ResourceItem(setOf(), "${MD}drawable/icon.png", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("icon", Res.drawable.icon)
}
