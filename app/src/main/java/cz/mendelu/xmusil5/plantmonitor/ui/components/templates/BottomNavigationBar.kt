package com.icontio.senscare_peresonal_mobile.ui.components.templates

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.navigation.Destination
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val iconFocused: ImageVector,
    val destination: Destination,
    val modifier: Modifier = Modifier
)

@Composable
fun BottomNavBar(
    navigation: INavigationRouter
){
    val currentBackStackEntry = navigation.getNavController().currentBackStackEntryAsState()

    val items = listOf(
        BottomNavItem(
            title = stringResource(id = R.string.plantsScreen),
            icon = ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
            iconFocused = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            destination = Destination.PlantsScreen
        ),
        BottomNavItem(
            title = stringResource(id = R.string.devicesScreen),
            icon = ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
            iconFocused = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            destination = Destination.DevicesScreen
        ),
        BottomNavItem(
            title = stringResource(id = R.string.profileScreen),
            icon = ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
            iconFocused = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            destination = Destination.ProfileScreen
        ),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        items.forEach{
            BottomNavItemView(
                item = it,
                currentBackstackEntry = currentBackStackEntry.value,
                onItemClick = {
                    navigation.getNavController().navigate(it.destination.route)
                }
            )
        }
    }
}

@Composable
fun BottomNavItemView(
    item: BottomNavItem,
    currentBackstackEntry: NavBackStackEntry?,
    onItemClick: (navItem: BottomNavItem) -> Unit
){
    var isSelected = item.destination.route == currentBackstackEntry?.destination?.route

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
        animationSpec = tween(
            durationMillis = 300
        )
    )
    val foregroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onPrimary,
        animationSpec = tween(
            durationMillis = 300
        )
    )
    val icon = if (isSelected) item.iconFocused else item.icon

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(35.dp)
                .width(45.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable {
                    onItemClick(item)
                }
        ){
            Icon(
                imageVector = icon,
                contentDescription = item.title,
                tint = foregroundColor,
                modifier = Modifier
                    .height(30.dp)
                    .aspectRatio(1f)
                    .padding(5.dp)
            )
        }
        Text(
            text = item.title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(top = 1.dp)
        )
    }

}