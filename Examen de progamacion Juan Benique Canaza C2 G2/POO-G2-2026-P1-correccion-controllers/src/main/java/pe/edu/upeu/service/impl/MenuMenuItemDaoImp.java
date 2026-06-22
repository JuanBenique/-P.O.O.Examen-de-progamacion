package pe.edu.upeu.service.impl;

import pe.edu.upeu.dto.MenuMenuItenTO;
import pe.edu.upeu.service.IMenuMenuItemDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MenuMenuItemDaoImp implements IMenuMenuItemDao {

    @Override
    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma) {
        List<MenuMenuItenTO> lista = new ArrayList<>();

        // 1. Botón de Salir
        lista.add(new MenuMenuItenTO("miprincipal", "/view/login.fxml",
                "Principal", "Salir", "Salir del Sistema", "S"));

        // 2. Mantenimiento: Categorías
        lista.add(new MenuMenuItenTO("micategoria", "/view/main_categoria.fxml",
                "Mantenimiento", "Categorías", "Gestión de Categorías", "T"));

        // 3. Mantenimiento: Productos
        lista.add(new MenuMenuItenTO("miproducto", "/view/main_producto.fxml",
                "Mantenimiento", "Productos", "Gestión de Productos", "T"));

        // 4. Mantenimiento: Clientes
        lista.add(new MenuMenuItenTO("micliente", "/view/main_cliente.fxml",
                "Mantenimiento", "Clientes", "Gestión de Clientes", "T"));

        // 5. Módulo: Ventas
        lista.add(new MenuMenuItenTO("miventa", "/view/main_venta.fxml",
                "Ventas", "Registro de Venta", "Gestionar Ventas", "T"));

        // 6. Módulo: Detalle de Venta
        lista.add(new MenuMenuItenTO("miventadetalle", "/view/main_ventadetalle.fxml",
                "Ventas", "Detalle de Venta", "Detalles de Venta", "T"));

        return lista;
    }

    @Override
    public Map<String, String[]> accesosAutorizados(List<MenuMenuItenTO> accesos) {
        Map<String, String[]> menuConfig = new HashMap<>();
        for (MenuMenuItenTO menu : accesos) {
            menuConfig.put(menu.getNombreObj(), new String[]{menu.getMenuNombre(), menu.getMenuItemNombre()});
        }
        return menuConfig;
    }
}
