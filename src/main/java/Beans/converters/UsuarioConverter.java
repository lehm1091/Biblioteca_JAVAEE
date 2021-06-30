/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.converters;

import entity.Usuarios;
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

@FacesConverter(value = "usuariosConverter")
public class UsuarioConverter implements Converter {

    private final static String DELIMITER = "~";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        String[] parts = value.split(DELIMITER);
        Usuarios result = new Usuarios();
        result.setIdUsuario(BigDecimal.valueOf(Integer.valueOf(parts[0])));
        result.setNombre(parts[1]);
        result.setDui(parts[2]);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof Usuarios)) {
            throw new RuntimeException(String.format("Inconvertable type: %s of value '%s'", value.getClass(), value));
        }

        System.out.println("en get as string");
        Usuarios usuario = (Usuarios) value;
        StringBuilder sb = new StringBuilder();
        sb
                .append(usuario.getIdUsuario())
                .append(DELIMITER)
                .append(usuario.getNombre())
                .append(DELIMITER)
                .append(usuario.getDui())
                .append(DELIMITER);

        String result = new String(sb);

        return result;
    }
}
