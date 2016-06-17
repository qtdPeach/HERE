# HERE (Home Exercise REminder) App

## Introduction

Many people have one or more exercise equipments in their home, however the equipments are not used neither regularly nor effectively by us. Even though the reason why people cannot use them regularly depends on individual habits and characteristics, three common reasons can be summrized.

- **Home exercise equipments are invisible.**
-- For this reason, we are not motivated by them because they are hanging up on the walls or just placed in our room just like furniture.
- **Home exercise equipments do not give us any feedback.**
-- Therefore, we can be easily bored to do exercise and we have to do our own fighting with a strong self-motivation.
- **Home exercise equipments have no ability to record user's activities.**
-- Due to the lack of exercise history, we cannot set and manage our fitness goals and progress systematically.

To address the three reasons above, we develop a system called **HERE (Home Exercise REminder)**. HERE system mainly consists of two modules, *HERE-agents* and *HERE-application (HERE-app)*. Out of that, this document introduces the goal and main functions of *HERE-app*, which controls overall exercise and manages information.


## Requirements

### Functional Requirements (Key Functions)

- **Welcome a user near agents with lights and sounds.**
In order to make users exercise at home when they enter their room, our HERE equipments should notify  where they are to be easily found ("HERE! Please use me!"). 

- **Monitor user's movement and provide real-time feedback.**
To provide those profits to users, the agents are required to send real-time sensor data and HERE android application should visualize the real-time feedback to the users.

- **Record user's activities.**
Users tend to exercise with what they are used to or what is easy to use. However, balanced exercise plans are required for a well-shaped body. For this reason, HERE application should show which exercise equipments have not been used recently and regularly. After users finish doing exercise, they would want to see how much they achieved for a week. To show their achievement and to show equipment usage histories, HERE application needs to store user's exercise records.


### Non-functional Requirements

- **Preserve prior home-exercising experience.**
Not changing the previous way to exercise, while minimizing the size and weight of attached sensors, is important requirement in the implementation.

- **Provide feedback as fast as possible.**
In order to make users have fun in doing home exercise with HERE, fast and accurate feedback is the most important feature required.

- **Deliver Easy-to-use experience.**
To motivate such weak-willed people, the process to use HERE should be simple and easy as possible.


## System Architecture of HERE-App

***HERE-app*** is connected with multiple ***HERE-agents***, and *HERE-app* receives real-time streaming data from agents and processes it. According to the analysis result, HERE-app generates feedback messages and sends them to a corresponding agent. For those tasks, HERE-app consists of two primary modules: (a) Bluetooth Manager, and (b) Exercise Manager. 

First, *Bluetooth Manager* manages BLE communication with HERE-agents. In order to exchange messages between agents and app when the user first enters her room or she is doing exercise, the BLE connection should be built. *Connection Manager* controls the connection of BLE. If a connection is established, *Agent Scanner* scans nearby agents and updates a list with discovered agents. When a user starts exercise with connected agents, *Sensor Data Receiver* receives measurements from agents.

Second, *Exercise Manager* (1) manages exercise data such as user's agents, routines, records, and (2) monitors exercise using received sensor data. In the detail of *Exercise Data Manager*, a user can register her own agents using *Agent Manager* and it manages user's agents with MAC IDs, user-defined names, and types of equipments. A user also can define her custom exercise routines using *Routine Manager*. *Record Manager* of *Exercise Data Manger* stores user's every exercise records and generates a calorie report and equipment usage report.

After a user starts exercise with a selected routine, *Exercise Monitor* analyzes sensor data from *Sensor Data Receiver*. Based on the analysis results, *Performance Recorder* draws a real-time graph of a connected HERE-agent, and keeps track of the number of user's exercise such as lifting a dumbbell.

## User Manual

### How to manage Agents

If a user launches HERE app, then HERE app automatically searches nearby Bluetooth devices. Among found devices, a user can add a HERE agent into her agent list. Unless a Bluetooth device is compatible with HERE, it is not allowed to add by the user. The MAC ID, name, and type of agents are used to make custom exercise routines and records, and they are also used to connect with its Bluetooth module to receive sensor data. The beacon information of an agent is used to distinguish HERE agents when a new agent is added in database.

***How to do this in HERE-app?***

1. Launch HERE-app
2. Click the navigation drawer menu
3. Click <My HERE Agents> menu on the navigation drawer
4. Wait for the update of pairable bluetooth devices list
5. Select one of the compatible HERE devices from the list
6. Enter the name of the new agent
7. Click 'Add Agent' button


### How to manage Routines

Using the registered HERE agents (i.e., equipments), a user can make a new custom exercise routine. In order to make a routine, the user has to select an agent and set a goal for the agent like target counts or lap time. A user can make a routine with up to five agents, and same agent can be used in duplicate. Once an exercise routine is created, a user is ready to start exercising with the routine. Each routine has the information of containing agents, so HERE app can connect with each agent in the routine with MAC ID.

***How to do this in HERE-app?***

1. Launch HERE-app
2. Click the navigation drawer menu
3. Click <My Exercise Routines> menu on the navigation drawer
4. In <Make A New Routine> section, you can make a new routine with your HERE agents
5. Select one of the registered agents from <My Here Agent> list
6. Set exercise goals for the selected agent. You can insert the number of sets, counts, or time (sec)
7. Click 'Add to Routine' button
8. If you want to add more agent into the new list, then repeat 5~7
9. After you finish adding agents in the list, then click the add button (+)
10. Name your new routine and click 'Add' button


### How to check and manage Records?

After finishing exercise with a certain exercise routine, an exercise record is stored in DB. Using records in the table, HERE app can also make some reports by analyzing the records. First, by examining all records, HERE app creates a weekly calorie report that shows consumption of calories during the last one week. Second, by grouping the records by each agent, HERE app creates an equipment report that shows usage histories for each agent. Two types of reports are graphically displayed on HERE app using *Jonas Gehring's GraphView* library (Android GraphView: http://www.android-graphview.org/).

***How to do this in HERE-app?***

1. Check my exercise records

- Launch HERE-app
- Click the navigation drawer menu
- Click <My Exercise Records> menu on the navigation drawer
- You can check your prizes and all the recent exercise records

2. Check my calorie report & equipment usage history

- Launch HERE-app
- Click <MY RECORDS> tab
- You can check your calorie report and equipment usage history


### How to start exercise?

First, you have to select one of my routines that you made. After that, click the floating action button on the right bottom of the screen after you launch HERE-app. If you meet a blue screen with a girl who is doing exercise, then you are ready to start exercise. You can click <Start Exercise> button if the selected routine is what you wanted. 

Once you start exercise, then the timer is automatically started and the sensor streaming data is displayed by the graph. If you want to skip current equipment, then click <Skip this> button and go to the next stage. If you finish all equipments in the routine, then the application shows the finish screen. In the finish screen, you can name the record of your activities, and the records are stored in DB. You can check your records in <My Record> tab or <My Exercise Routine> menu.


## Database (hereDB)

1. Table ***MyInformation***

 Records in *MyInformation* table contain user-id, user-name, user-nick, user-sex, user-height, user-weight, user-device-id.

2. Table ***MyHereAgents***

 Records in *MyHereAgents* table contain agent-mac-id, agent-name, agent-type, agent-beacon-major-id, agent-beacon-minor-id.

3. Table ***MyRoutines***

 Records in *MyRoutines* table contain routine-id, routine-name, and multiple set of IDs (agent-mac-id) and goals (agent-goal) for every agent in the routine.

4. Table ***MyRecords***

 Records in *MyRecords* table contain record-id, record-name, record-datetime, and multiple set of agent IDs (agent-mac-id) and records (agent-done) for every agent in the new record.


## Libraries

- Android GraphView: http://www.android-graphview.org/


## Contributors

- Sunggeun Ahn (topmaze@kaist.ac.kr), Human Computer Interaction Lab, KAIST
- Young-Min Baek (ymbaek@se.kaist.ac.kr), Software Engineering Lab, KAIST
- Jiyoung Song (jysong@se.kaist.ac.kr), Software Engineering Lab, KAIST


 


