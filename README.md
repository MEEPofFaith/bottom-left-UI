# Bottom Left UI Lib
A Mindustry libary mod that solves the biggest territory war in Mindustry modding:
Adding UI to the bottom-left corner of the screen.

## How to use

Simply call `BLSetup.addTable` to add another table to the bottom-left corner UI.
See [BLUI Example](https://github.com/MEEPofFaith/blui-example/blob/25dcb595f2614edaa6ee3e2066950e5e10291503/src/bluiexample/BLUIExample.java#L15)
or [Testing Utilities](https://github.com/MEEPofFaith/testing-utilities-java/blob/eba541bb4310bac9b3f09962e84d0074dcbf557d/src/testing/util/Setup.java#L25) for usage examples.

## How to add to your mod

Add `compileOnly "com.github.MEEPofFaith:bottom-left-ui:<release version>"` to your `build.gradle`'s dependencies as seen [here](https://github.com/MEEPofFaith/blui-example/blob/25dcb595f2614edaa6ee3e2066950e5e10291503/build.gradle#L41).
