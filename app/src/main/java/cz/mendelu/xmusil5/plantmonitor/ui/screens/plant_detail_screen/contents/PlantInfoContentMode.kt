package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents

import cz.mendelu.xmusil5.plantmonitor.R

enum class PlantInfoContentMode(val nameId: Int, val iconId: Int) {
    BASIC_INFO(R.string.basicInfo, R.drawable.ic_info),
    NOTES(R.string.notes, R.drawable.ic_note),
    MEASUREMENTS(R.string.measurements, R.drawable.ic_list),
    CHARTS(R.string.charts, R.drawable.ic_chart);
}