**Remember: _It's all about the looks._**

![MPAndroidChart](https://raw.github.com/MGaetan89/MPAndroidChart/master/design/feature_graphic_smaller.png)

[![Build Status](https://travis-ci.org/MGaetan89/MPAndroidChart.svg?branch=master)](https://travis-ci.org/MGaetan89/MPAndroidChart)
[![codecov](https://codecov.io/gh/MGaetan89/MPAndroidChart/branch/master/graph/badge.svg)](https://codecov.io/gh/MGaetan89/MPAndroidChart)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Release](https://img.shields.io/github/release/PhilJay/MPAndroidChart.svg?style=flat)](https://jitpack.io/#PhilJay/MPAndroidChart)

:zap: A powerful & easy to use chart library for Android :zap:

[**Charts**](https://github.com/danielgindi/Charts) is the iOS version of this library

## Original Project

This project is built on top of the popular [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) library. Feel free to check it out, and show your support to [PhilJay](https://github.com/PhilJay).

### Demo

Download the [MPAndroidChart Example App](https://play.google.com/store/apps/details?id=com.xxmassdeveloper.mpchartexample) or look at the [source code](https://github.com/MGaetan89/MPAndroidChart/tree/master/MPChartExample).

[![ScreenShot](https://github.com/MGaetan89/MPAndroidChart/blob/master/design/video_thumbnail.png)](https://www.youtube.com/watch?v=ufaK_Hd6BpI)

## Goals of this fork

- Follow Google's direction regarding `minSdkVersion`. As such, this project requires Android 4.0 and up (API level 14+)
- Increase test coverage to fix the issues reported against this project, as well as the [original project](https://github.com/PhilJay/MPAndroidChart)

## Features

You can have a look at the core features of this library [here](https://github.com/PhilJay/MPAndroidChart/wiki/Core-Features).

## Usage

In order to use this library in your project, follow these two steps:

### 1. Add JitPack repository

In your root `build.gradle` file, add the following if it is not already there:

```gradle
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```

### 2. Add the library dependency

In your module `build.gradle` file, add the following dependency:

```gradle
dependencies {
	implementation 'com.github.PhilJay:MPAndroidChart:v3.0.3'
}
```

## Documentation

See the [**documentation**](https://github.com/PhilJay/MPAndroidChart/wiki) for examples and general use of MPAndroidChart.

See the [**javadocs**](https://jitpack.io/com/github/PhilJay/MPAndroidChart/v3.0.3/javadoc/) for more advanced documentation.

## Chart types

 - **LineChart** with legend, simple design

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/simpledesign_linechart4.png)

 - **LineChart** with legend, simple design

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/simpledesign_linechart3.png)

 - **LineChart** cubic lines

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/cubiclinechart.png)

 - **LineChart** gradient fill

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/line_chart_gradient.png)

 - **Combined-Chart** barchart and linechart

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/combined_chart.png)

 - **BarChart** with legend, simple design

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/simpledesign_barchart3.png)

 - **BarChart** grouped DataSets

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/groupedbarchart.png)

 - **Horizontal-BarChart**

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/horizontal_barchart.png)

 - **PieChart** with selection, ...

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/simpledesign_piechart1.png)

 - **ScatterChart** with squares, triangles, circles, ... and more

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/scatterchart.png)

 - **CandleStickChart** for financial data

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/candlestickchart.png)

 - **BubbleChart** area covered by bubbles indicates the yValue

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/bubblechart.png)

 - **RadarChart** spider web chart

![alt tag](https://raw.github.com/MGaetan89/MPAndroidChart/master/screenshots/radarchart.png)

## License

Copyright 2018 Philipp Jahoda

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Special Thanks

These people rock!

- [danielgindi](https://github.com/danielgindi) - Daniel Gindi
- [mikegr](https://github.com/mikegr) - Michael Greifeneder
- [tony](https://github.com/tonypatino-monoclesociety) - Tony
- [almic](https://github.com/almic) - Mick A.
- [jitpack.io](https://github.com/jitpack-io) - JitPack.io
