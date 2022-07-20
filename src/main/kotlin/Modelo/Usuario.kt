import Modelo.UserPrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import com.pedidosya.courier.common.dynamodb.repository.IDynamoEntity
import com.pedidosya.courier.common.dynamodb.repository.IPrimaryKey

data class Usuario(val id:Int,val nombre:String?,val email:String?) : IDynamoEntity {

  @get:JsonIgnore
  override val primaryKey: IPrimaryKey by lazy {
    UserPrimaryKey(this.id.toString())
  }






}
