/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.converters;

import entity.Autores;
import java.math.BigDecimal;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "AutoresConverter")
public class AutoresConverter implements Converter {

    private final static String DELIMITER = "~";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        String[] parts = value.split(DELIMITER);
        Autores result = new Autores();
        result.setIdAutor(BigDecimal.valueOf(Integer.valueOf(parts[0])));
        result.setNacionalidad(parts[1]);
        result.setNombre(parts[2]);
        result.setSexo(parts[3]);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (!(value instanceof Autores)) {
            throw new RuntimeException(String.format("Inconvertable type: %s of value '%s'", value.getClass(), value));
        }

        Autores autor = (Autores) value;
        StringBuilder sb = new StringBuilder();
        sb
                .append(autor.getIdAutor())
                .append(DELIMITER)
                .append(autor.getNacionalidad())
                .append(DELIMITER)
                .append(autor.getNombre())
                .append(DELIMITER)
                .append(autor.getSexo());

        String result = new String(sb);

        return result;
    }
}
