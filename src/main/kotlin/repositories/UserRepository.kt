
import Modelo.UserPrimaryKey
import com.pedidosya.courier.common.configuration.IConfig
import com.pedidosya.courier.common.dynamodb.repository.ExtendableDynamoDbRepository
import com.pedidosya.courier.common.dynamodb.repository.IPrimaryKey
import configuration.Config
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue


class UserRepository(config: IConfig, dynamoDbClient:DynamoDbAsyncClient):ExtendableDynamoDbRepository<Usuario,IPrimaryKey>(config, dynamoDbClient){
  override fun buildCreateItemAttributes(model: Usuario): Map<String, AttributeValue> {
    return mapOf(
      "nombre" to string(model.nombre),
      "email" to string(model.email)
    )
  }

  override fun transformResult(item: Map<String, AttributeValue>): Usuario {
    val userId= requireNotNull(item["userId"]).n()
    val userName=item["nombre"]?.n()
    val userEmail=item["email"]?.n()

    return Usuario(
      id=userId.toInt(),
      nombre=userName,
      email=userEmail
    )
  }

  suspend fun getUser(userId:Int):Usuario?{
    return this.get(
      UserPrimaryKey(userId.toString()),
      pkName = "userId"
    )
  }

}
