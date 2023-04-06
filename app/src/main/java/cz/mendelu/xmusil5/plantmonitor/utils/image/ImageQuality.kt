package cz.mendelu.xmusil5.plantmonitor.utils.image

enum class ImageQuality(val maxPixelDensity: Long) {
    SMALL(maxPixelDensity = 76800), // equivalent to 320*240
    MEDIUM(maxPixelDensity = 786432), // equivalent to 1024*768
    LARGE(maxPixelDensity = 2073600); // equivalent to 1920*1080
}