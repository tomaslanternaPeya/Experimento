class Pedido {
    private var restaurante: Restaurante
    private var usuario: Usuario
    private var horaCreacion: String?

    constructor(restaurante: Restaurante, usuario: Usuario, horaCreacion: String?) {
        this.restaurante = restaurante
        this.usuario = usuario
        this.horaCreacion = horaCreacion
    }


}
