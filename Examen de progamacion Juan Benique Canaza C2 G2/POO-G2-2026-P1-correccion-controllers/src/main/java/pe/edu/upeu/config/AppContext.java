package pe.edu.upeu.config;

import pe.edu.upeu.controller.*;
import pe.edu.upeu.repository.*;
import pe.edu.upeu.service.*;
import pe.edu.upeu.service.impl.*;
import pe.edu.upeu.utils.ConsultaDNI;

import java.util.HashMap;
import java.util.Map;

public class AppContext {

    private static AppContext instance;

    public static synchronized AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

    private final Map<Class<?>, Object> contenedor = new HashMap<>();

    private AppContext() {
        registrarRepositorios();
        registrarServicios();
        registrarControladores();
    }

    private void registrarRepositorios() {
        registrar(CategoriaRepository.class,    new CategoriaRepository());
        registrar(ClienteRepository.class,      new ClienteRepository());
        registrar(ProductoRepository.class,     new ProductoRepository());
        registrar(VentaRepository.class,        new VentaRepository());
        registrar(VentaDetalleRepository.class, new VentaDetalleRepository());
        registrar(UsuarioRepository.class,      new UsuarioRepository());
    }

    private void registrarServicios() {
        registrar(ConsultaDNI.class,          new ConsultaDNI());
        registrar(IMenuMenuItemDao.class,     new MenuMenuItemDaoImp());
        registrar(IUsuarioService.class,      new UsuarioServiceImp(getBean(UsuarioRepository.class)));
        registrar(ICategoriaService.class,    new CategoriaServiceImp(getBean(CategoriaRepository.class)));
        registrar(IClienteService.class,      new ClienteServiceImp(getBean(ClienteRepository.class)));
        registrar(IProductoService.class,     new ProductoServiceImp(getBean(ProductoRepository.class)));
        registrar(IVentaService.class,        new VentaServiceImp(getBean(VentaRepository.class)));
        registrar(IVentaDetalleService.class, new VentaDetalleServiceImp(getBean(VentaDetalleRepository.class)));
    }

    private void registrarControladores() {
        registrar(LoginController.class,         new LoginController(getBean(IUsuarioService.class)));
        registrar(MainGuiController.class,       new MainGuiController(getBean(IMenuMenuItemDao.class)));
        registrar(CategoriaController.class,     new CategoriaController(getBean(ICategoriaService.class)));
        registrar(ClienteController.class,       new ClienteController(getBean(IClienteService.class)));
        registrar(ProductoController.class,      new ProductoController(getBean(IProductoService.class), getBean(ICategoriaService.class)));
        registrar(VentaController.class,         new VentaController(getBean(IVentaService.class), getBean(IClienteService.class), getBean(IUsuarioService.class)));
        registrar(VentaDetalleController.class,  new VentaDetalleController(getBean(IVentaDetalleService.class), getBean(IVentaService.class), getBean(IProductoService.class)));
    }

    private void registrar(Class<?> tipo, Object bean) {
        contenedor.put(tipo, bean);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> tipo) {
        Object bean = contenedor.get(tipo);
        if (bean == null) {
            bean = contenedor.values().stream()
                    .filter(b -> tipo.isAssignableFrom(b.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Bean no encontrado: " + tipo.getName() +
                                    "\n→ ¿Lo registraste en AppContext?"));
        }
        return (T) bean;
    }
}