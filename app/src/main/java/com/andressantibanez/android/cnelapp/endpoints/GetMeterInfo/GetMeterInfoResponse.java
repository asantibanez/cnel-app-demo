package com.andressantibanez.android.cnelapp.endpoints.GetMeterInfo;

public class GetMeterInfoResponse {

    public String codigoResultado;
    public String mensajeResultado;

    public Cliente cliente;

    public static class Cliente {
        public String codigo_cuenta;
        public String nombre;
        public String deuda;
        public String fecha;
    }
}
