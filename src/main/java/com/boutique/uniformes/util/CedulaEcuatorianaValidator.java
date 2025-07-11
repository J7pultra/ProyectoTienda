package com.boutique.uniformes.util;

public class CedulaEcuatorianaValidator {
    public static boolean isValid(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) return false;
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) return false;
        int[] coef = {2,1,2,1,2,1,2,1,2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int val = Character.getNumericValue(cedula.charAt(i)) * coef[i];
            if (val > 9) val -= 9;
            suma += val;
        }
        int digitoVerificador = 10 - (suma % 10);
        if (digitoVerificador == 10) digitoVerificador = 0;
        return digitoVerificador == Character.getNumericValue(cedula.charAt(9));
    }
}
