class Restaurante {
    private var id: Int
    private var nombre: String?
    private var pedidos:List<Pedido>

    constructor(id: Int, nombre: String?) {
        this.id = id
        this.nombre = nombre
        this.pedidos=mutableListOf()
    }

    fun addPedido(unPedido:Pedido){
        if(unPedido.validar()){
            pedidos.add(unPedido)
        }
    }
}