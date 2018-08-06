package com.dev_station.shekhar.agecalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    private TextView startDateDisplay;
    private TextView endDateDisplay;
    private TextView txtResult;
    private TextView txtMonthDays;
    private TextView txtWeekDays;
    private TextView tvTime;
    private TextView txtTotalDays;
    private Button startPickDate;
    private Button endPickDate;
    private Button btnCalculate;
    private Button btnTime;
    private Calendar startDate;
    private Calendar endDate;
    private int mHour, mMinute, mSeconds;

    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;
    private Calendar timeCalender;
    private Calendar currentTimeCalender;
    SimpleDateFormat timeFormat;

    /**
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeFormat = new SimpleDateFormat("hh:mm aa");
        currentTimeCalender = Calendar.getInstance();
        /*  capture our View elements for the start date function   */
        startDateDisplay = (TextView) findViewById(R.id.startDateDisplay);
        startPickDate = (Button) findViewById(R.id.btnStartDate);

        /* get the current date */
        startDate = Calendar.getInstance();

        /* add a click listener to the button   */
        startPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startDateDisplay, startDate);
            }
        });

        /* capture our View elements for the end date function */
        endDateDisplay = (TextView) findViewById(R.id.endDateDisplay);
        endPickDate = (Button) findViewById(R.id.btnEndDate);
        btnTime = (Button) findViewById(R.id.btnTime);

        /* get the current date */
        endDate = Calendar.getInstance();

        /* add a click listener to the button   */
        endPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(endDateDisplay, endDate);
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtMonthDays = (TextView) findViewById(R.id.txtMonthDay);
        txtWeekDays = (TextView) findViewById(R.id.txtWeekDays);
        txtTotalDays = (TextView) findViewById(R.id.txtTotalDays);
        tvTime = (TextView) findViewById(R.id.tvTime);
        btnCalculate = (Button) findViewById(R.id.btnCalculateAge);
        tvTime.setText((timeFormat.format(currentTimeCalender.getTime())).toUpperCase());
        /* add a click listener to the button   */
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar startDate1 = Calendar.getInstance();

                startDate1.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH) + 1, startDate.get(Calendar.DAY_OF_MONTH));
                Calendar endDate1 = Calendar.getInstance();
                endDate1.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH) + 1, endDate.get(Calendar.DAY_OF_MONTH));

                DateCalculator dateCalculator = DateCalculator.calculateAge(startDate1, endDate1);
                String age = "Age: " + dateCalculator.getYear() + " Years " + dateCalculator.getMonth() + " Months " + dateCalculator.getDay() + " Days";
                int num_weeks = (int) dateCalculator.getTotalDay() / 7;
                int num_months = dateCalculator.getYear() * 12 + dateCalculator.getMonth();
                System.out.println(dateCalculator.getYear());
                txtResult.setText(age);
                txtTotalDays.setText("" + dateCalculator.getTotalDay() + " Days");
                txtWeekDays.setText("" + num_weeks + " Weeks " + dateCalculator.getTotalDay() % 7 + " Days");
                txtMonthDays.setText("" + num_months + " Months " + dateCalculator.getDay() + " Days ");

                txtTotalDays.append("\n" + dateCalculator.getTotalDay() * 24 + " Hours");
                txtTotalDays.append("\n" + (dateCalculator.getTotalDay() * 24) * 60 + " Minutes");
                txtTotalDays.append("\n" + (dateCalculator.getTotalDay() * 24) * 3600 + " Seconds");

                //showDateDialog(endDateDisplay, endDate);

                try {
                    Date date1 = timeFormat.parse("05:25 am");
                    Date date2 = timeFormat.parse("08:30 pm");
                    long mills = date1.getTime() - date2.getTime();
                    Log.v("TimeTotalMilli", "" + mills);
                    Log.v("TimeMilli1", "" + date1.getTime());
                    Log.v("TimeMilli2", "" + date2.getTime());
                    int hours = (int) (mills / (1000 * 60 * 60));
                    int mins = (int) (mills / (1000 * 60)) % 60;
                    Log.v("TimeHour",hours+"");
                    Log.v("TimeMinuts",mins+"");
                    String diff = hours + ":" + mins; // updated value every1 second
//                    txtTotalDays.setText(diff);
                    long totalDays=dateCalculator.getTotalDay();
                    long totalDaysMilli= TimeUnit.DAYS.toMillis(totalDays);

                    long seconds = (totalDaysMilli+(mills)) / 1000;
                    long minutes = seconds / 60;
                    long hoursCal = minutes / 60;
                    long days = hoursCal / 24;
                    String time = days + ":" + hoursCal + ":" + minutes + ":" + seconds ;
                    txtTotalDays.append("\n"+time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /* display the current date (this method is below)  */
        updateDisplay(startDateDisplay, startDate);
        updateDisplay(endDateDisplay, endDate);
    }

    public void getTime() {
        timeCalender = Calendar.getInstance();
        mHour = timeCalender.get(Calendar.HOUR_OF_DAY);
        mMinute = timeCalender.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";

                        } else {
                            AM_PM = "PM";
                            mHour = mHour - 12;
                        }
                        tvTime.setText(String.format("%02d:%02d", hourOfDay, minute) + " " + AM_PM);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    /**
     * update the date at ui text view
     *
     * @param dateDisplay text view where the date will be shown
     * @param date        selected date
     */
    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
                        .append(date.get(Calendar.MONTH) + 1).append("-")
                        .append(date.get(Calendar.YEAR)).append(" "));

    }

    /**
     * display the date dialog
     *
     * @param dateDisplay
     * @param date
     */
    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private OnDateSetListener dateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }


}
