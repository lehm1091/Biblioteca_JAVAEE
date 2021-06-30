/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.converters;

import entity.Ejemplares;
import entity.Obras;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "ejemplaresConverter")
public class EjemplarConverter implements Converter {

    private final static String DELIMITER = "~";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        String[] parts = value.split(DELIMITER);
        Ejemplares result = new Ejemplares();
        result.setIdEjemplar(BigDecimal.valueOf(Integer.valueOf(parts[0])));
        result.setNumeroEjemplar(parts[1]);
        Obras obra_ = new Obras();
        obra_.setTitulo(parts[2]);
        result.setObra(obra_);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof Ejemplares)) {
            throw new RuntimeException(String.format("Inconvertable type: %s of value '%s'", value.getClass(), value));
        }

        System.out.println("en get as string");
        Ejemplares ejemplar = (Ejemplares) value;
        StringBuilder sb = new StringBuilder();
        sb
                .append(ejemplar.getIdEjemplar())
                .append(DELIMITER)
                .append(ejemplar.getNumeroEjemplar())
                .append(DELIMITER)
                .append(ejemplar.getObra().getTitulo())
                .append(DELIMITER);

        String result = new String(sb);

        return result;
    }
}
