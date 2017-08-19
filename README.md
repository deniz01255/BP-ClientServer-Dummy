# BP-AndroidApp
Android App für das Bachelorpraktikum Datenerfassung für ein barrierefreies routing.


## Dependencies

- OKHttp
- osmdroid
- [BP Common Data Model](https://github.com/Vincinator/BP-Common)
- [stepstone-tech/android-material-stepper](https://github.com/stepstone-tech/android-material-stepper)
- EventBus [greenrobot eventBus Documentation](http://greenrobot.org/eventbus/documentation/)


## Usage


### Step 1.
In order to place a new Obstacle on the openstreetmap roads, you need to retrieve road information.
long press near the place where you want to add the obstacle.

![alt text](doc/appScreenshot_1.png)

![alt text](doc/appScreenshot_2.png)

### Step 2.
The nearby roads are highlighted with an overlay.
You can click on the overlay to place the obstacle.

![alt text](doc/appScreenshot_3.png)

### Step 3.
The Action Button appears. Click the action Button if you are finished with the positioning.
Note: you can replace the position by clicking on an other spot at the highlighted road.
Note: you can also get other roads, by long pressing near the place where you want to get other roads.

![alt text](doc/appScreenshot_4.png)


### Step 4. After you have completed the Step-By-Step Dialog, your collected data has been send to the
server. The new Obstacle is now displayed.

![alt text](doc/appScreenshot_5.png)