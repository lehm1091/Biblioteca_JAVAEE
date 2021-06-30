/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.converters;

import entity.Autores;
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

@FacesConverter(value = "obrasConverter")
public class ObrasConverter implements Converter {

    private final static String DELIMITER = "~";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        String[] parts = value.split(DELIMITER);
        Obras result = new Obras();
        result.setIdObra(BigDecimal.valueOf(Integer.valueOf(parts[0])));
        result.setFechaPublicacion(new Date());
        result.setAutoresList(new ArrayList<Autores>());
        result.setEjemplaresList(new ArrayList<Ejemplares>());
        result.setEditora(parts[1]);
        result.setNacionalidad(parts[2]);
        result.setTitulo(parts[3]);
        System.out.println("en get as object");
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof Obras)) {
            throw new RuntimeException(String.format("Inconvertable type: %s of value '%s'", value.getClass(), value));
        }
        
        System.out.println("en get as string");
        Obras obra = (Obras) value;
        StringBuilder sb = new StringBuilder();
        sb
                .append(obra.getIdObra())
                .append(DELIMITER)
                .append(obra.getEditora())
                .append(DELIMITER)
                .append(obra.getNacionalidad())
                .append(DELIMITER)
                .append(obra.getTitulo());
        
        String result = new String(sb);
        
        return result;
    }
}
