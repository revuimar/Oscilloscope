# Oscilloscope
Oscilloscope project in Javafx

This was build with NetBeans IDE 8.2 on jdk1.8.0_131

To run the program go to `dist` directory and run `Oscilloscope.jar` exacutable jar file.

How to use

Select CSV data source files.

![menu view](https://github.com/revuimar/Oscilloscope/blob/master/manual_snaps/menu.png)

You can load data on both X and Y channel.
You can find some sample CSV files in `csv_examples` directory.
After loading proper files select source channel in "Source" `ComboBox`.

![menu view](https://github.com/revuimar/Oscilloscope/blob/master/manual_snaps/source.png)

After that press "Start" `button`  on top to display selected source.

When the chart is dislpayed you can alter it by using `Knobs`

![menu view](https://github.com/revuimar/Oscilloscope/blob/master/manual_snaps/knobs.png)

`Sampling` Knob elimnates every nth sample except for everyone `1` state which is default and displays full set.

`X Pos`and `Y Pos` move the chart in x and y directions.

`V/Div` scales the Y axis.

`Sec/Div` scales the X axis showing more periods or in case of XY channel mode sales X axis.

Below every knob there is a `TextBox` displaying current value of the variables.
You can change them either by manipulating the Knob or by enterig the desired value in the `TextBox` and pressing <kbd>Enter</kbd>.
In case of `X Pos` and `Y Pos` you can edit only 1 sample at the time by entering "-1" in left or up direction accordingly or "1" in right or up direction accordingly.
