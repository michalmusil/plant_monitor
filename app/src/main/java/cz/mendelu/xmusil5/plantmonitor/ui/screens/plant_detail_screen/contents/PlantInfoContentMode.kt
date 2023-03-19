package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents

import cz.mendelu.xmusil5.plantmonitor.R

enum class PlantInfoContentMode(val nameId: Int) {
    BASIC_INFO(R.string.basicInfo),
    MEASUREMENTS(R.string.measurements),
    CHARTS(R.string.charts);
}