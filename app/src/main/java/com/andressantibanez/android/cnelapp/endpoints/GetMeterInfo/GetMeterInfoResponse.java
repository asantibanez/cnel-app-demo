package com.andressantibanez.android.cnelapp.endpoints.GetMeterInfo;

/**
 * Response example
 * {
    "codigoResultado": "00",
    "mensajeResultado": "Datos Correctos",
    "codigo_cuenta": "1376245",
    "listaconsumo": null,
    "cliente": {
        "codigo_cuenta": "1376245",
        "nombre": "SANTIBANEZ BRIONES., ANDRES DANIEL",
        "codigo_resultado": "00",
        "deuda": "22.97",
        "fecha": "07/03/2014"
    },
    "listapago": null
    }
 */

public class GetMeterInfoResponse {

    public Cliente cliente;

    public static class Cliente {
        public String codigo_cuenta;
        public String nombre;
        public String deuda;
        public String fecha;
    }
}
