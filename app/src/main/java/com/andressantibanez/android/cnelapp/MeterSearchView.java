package com.andressantibanez.android.cnelapp;

public interface MeterSearchView {

    void enableSearchButton(boolean enable);
    void showLoadingIndicator(boolean show);
    void displayErrorMessage(String errorMessage);

    void showResults(boolean show);
    void displayMeterInfo(String name, String debt, String date);

}
