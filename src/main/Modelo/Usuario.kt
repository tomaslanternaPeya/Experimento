class Usuario {
    private var id: Int
    private var nombre: String?
    private var email: String?
    private var pedidos:List<Pedido>

    constructor(id: Int, nombre: String?, email: String?) {
        this.id = id
        this.nombre = nombre
        this.email = email
        this.pedidos=mutableListOf()
    }

    fun setPedido(unPedido:Pedido) {
        if(unPedido.validar()){
            pedidos.add(unPedido)
            unPedido.getResto().addPedido(unPedido)
        }
    }




}