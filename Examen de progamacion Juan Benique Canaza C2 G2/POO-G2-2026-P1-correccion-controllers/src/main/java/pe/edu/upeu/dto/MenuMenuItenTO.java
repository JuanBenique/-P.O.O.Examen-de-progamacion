package pe.edu.upeu.dto;

public class MenuMenuItenTO {

    private String nombreObj;
    private String url;
    private String menunombre;
    private String menuitemnombre;
    private String titulo;
    private String estado;

    public MenuMenuItenTO() {
    }

    public MenuMenuItenTO(String nombreObj, String url, String menunombre, String menuitemnombre, String titulo, String estado) {
        this.nombreObj = nombreObj;
        this.url = url;
        this.menunombre = menunombre;
        this.menuitemnombre = menuitemnombre;
        this.titulo = titulo;
        this.estado = estado;
    }

    // --- GETTERS BLINDADOS ---

    public String getNombreObj() { return nombreObj; }
    public String getNombreobj() { return nombreObj; }
    public String getIdNombreObj() { return nombreObj; } // ¡AQUÍ ESTÁ LA CORRECCIÓN!
    public void setNombreObj(String nombreObj) { this.nombreObj = nombreObj; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMenunombre() { return menunombre; }
    public String getMenuNombre() { return menunombre; }
    public void setMenunombre(String menunombre) { this.menunombre = menunombre; }

    public String getMenuitemnombre() { return menuitemnombre; }
    public String getMenuItemNombre() { return menuitemnombre; }
    public void setMenuitemnombre(String menuitemnombre) { this.menuitemnombre = menuitemnombre; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
