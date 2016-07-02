package com.itsaunixsystem.marinara;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * @author: ajdt on 6/26/16.
 * @description: a dialog preference allowing user to select the minutes/seconds for a time range
 * NOTE: most of this code is heavily borrowed from:
 *                          https://developer.android.com/guide/topics/ui/settings.html#Custom
 */
public class MinuteSecondPreferenceDialog extends DialogPreference {

    // _persisted_millisec combines minutes/seconds and is used to persist time setting
    private long _persisted_millisec ;

    // store value currently selected in given number picker
    private long _minutes_picker_value ;
    private long _seconds_picker_value ;

    public MinuteSecondPreferenceDialog(Context context, AttributeSet attr) {
        super(context, attr) ;

        this.setDialogLayoutResource(R.layout.dialog_time_picker) ;
        this.setPositiveButtonText(android.R.string.ok) ;
        this.setNegativeButtonText(android.R.string.cancel) ;
        this.setDialogIcon(null) ; // TODO: have an icon later?
    }

    /**
     * Set initial value of minutes/seconds pickers and bind them to data members via callback
     * functions.
     * NOTE: duration is saved in millisec for simplicity, but UI displays in min/sec format
     * @param view a container view with all content of dialog UI
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view) ;

        // get the min/sec UI elements and obtain minute/second values from millisec
        NumberPicker minutes_np = (NumberPicker) view.findViewById(R.id.minutes_np) ;
        NumberPicker seconds_np = (NumberPicker) view.findViewById(R.id.seconds_np) ;
        long minutes = (_persisted_millisec / (1000 * 60)) % 60 ;
        long seconds = (_persisted_millisec / 1000) % 60 ;

        // set min, max and initial values
        minutes_np.setMinValue(0) ;
        minutes_np.setMaxValue(59) ;
        minutes_np.setValue((int) minutes) ;
        seconds_np.setMinValue(0) ;
        seconds_np.setMaxValue(59) ;
        seconds_np.setValue((int) seconds) ;


        // set value changed listeners
        minutes_np.setOnValueChangedListener(
                new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int old_value, int new_value) {
                        MinuteSecondPreferenceDialog.this._minutes_picker_value = new_value ;
                    }
                }
        );
        seconds_np.setOnValueChangedListener(
                new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int old_value, int new_value) {
                        MinuteSecondPreferenceDialog.this._seconds_picker_value = new_value;
                    }
                }
        );




    }

    /****************************** Set/Get values ******************************/

    /**
     * Save new value on close if user indicated it should be saved
     * @param save_millisec if true, ok button was clicked; result should be saved
     */
    @Override
    public void onDialogClosed(boolean save_millisec) {
        if (!save_millisec)
            return ;

        _persisted_millisec = _minutes_picker_value * 60 * 1000 + _seconds_picker_value * 1000 ;
        persistLong(_persisted_millisec) ;
    }

    @Override
    protected void onSetInitialValue(boolean restore_persisted_value, Object default_value) {

        if (restore_persisted_value) { // have a previously saved  value to restore
            _persisted_millisec = this.getPersistedLong(R.integer.default_timer_millisec) ;

        } else { // no previously saved values, use default
            _persisted_millisec = (Long) default_value ;
            persistLong(_persisted_millisec) ;
        }
    }

    /**
     * returns Long object with default value of _persisted_millisec.
     * Used to obtain second param of onSetInitialValue()
     * @param a
     * @param index
     * @return
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return new Long(a.getInt(index, R.integer.default_timer_millisec)) ;
    }

    /****************************** Saving/Restoring dialog ******************************/

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable super_state = super.onSaveInstanceState() ;

        if (this.isPersistent()) {
            return super_state ;
        }

        final SavedMinSecDialogState saved_state = new SavedMinSecDialogState(super_state) ;
        saved_state._saved_value = _persisted_millisec;

        return saved_state ;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedMinSecDialogState.class)) {
            super.onRestoreInstanceState(state) ;
            return ;
        }

        SavedMinSecDialogState min_sec_state = (SavedMinSecDialogState) state ;
        super.onRestoreInstanceState(min_sec_state.getSuperState()) ;

        // restore saved millisec
        _persisted_millisec = min_sec_state._saved_value ;
        // NOTE: i expect onBindDialogView() to restore the minute/second selection of the
        // NumberPicker widgets, so it's not done here
    }



    /****************************** Saving State ******************************/

    /**
     * class used to save MinuteSecondPreferenceDialog if screen is rotated or
     * in other cases when an instance is temporarily destroyed
     */
    private static class SavedMinSecDialogState extends Preference.BaseSavedState {
        long _saved_value ;

        public SavedMinSecDialogState(Parcelable super_state) {
            super(super_state) ;
        }

        public SavedMinSecDialogState(Parcel source) {
            super(source) ;

            _saved_value = source.readLong() ; // TODO: read differences between Parcel and Parcelable
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags) ;

            dest.writeLong(_saved_value) ;
        }

        public static final Parcelable.Creator<SavedMinSecDialogState> CREATOR =
                new Parcelable.Creator<SavedMinSecDialogState>() {
                    public SavedMinSecDialogState createFromParcel(Parcel in) {
                        return new SavedMinSecDialogState(in) ;
                    }

                    public SavedMinSecDialogState[] newArray(int size) {
                        return new SavedMinSecDialogState[size] ;
                    }
                } ;
    }
}

