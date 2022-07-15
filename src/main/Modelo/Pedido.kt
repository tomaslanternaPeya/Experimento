class Pedido {
    private var restaurante: Restaurante
    private var usuario: Usuario
    private var horaCreacion: String?
    private var horaEntrega

    constructor(restaurante: Restaurante, usuario: Usuario, horaCreacion: String?) {
        this.restaurante = restaurante
        this.usuario = usuario
        this.horaCreacion = horaCreacion
    }

    fun setHoraEntrega(hora:Date):Boolean{
        if(hora<this.horaCreacion){
            return false
        }
        this.horaEntrega=hora
        return true
    }

    fun getResto():Restaurante{
        return this.restaurante
    }

    fun validar():Boolean{
        return this.restaurante!=null
                && this.usuario!=null
                && this.horaCreacion!=null
    }
}