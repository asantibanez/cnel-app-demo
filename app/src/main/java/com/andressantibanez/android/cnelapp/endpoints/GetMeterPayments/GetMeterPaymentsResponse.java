package com.andressantibanez.android.cnelapp.endpoints.GetMeterPayments;

import java.util.ArrayList;

public class GetMeterPaymentsResponse {

    public ArrayList<Pago> listapago;

    public static class Pago {
        public String codigo_cuenta;
        public String nombre;
        public String pago;
        public String fecha;
    }

}
