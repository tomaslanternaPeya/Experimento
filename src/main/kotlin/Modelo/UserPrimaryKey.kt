package Modelo

import com.pedidosya.aws.sdk.dynamodb.PrimaryKeyValue
import com.pedidosya.courier.common.dynamodb.repository.DynamoDbConverter
import com.pedidosya.courier.common.dynamodb.repository.IPrimaryKey

data class UserPrimaryKey(val id:String) : IPrimaryKey,DynamoDbConverter{

  override fun buildPrimaryKey(): PrimaryKeyValue {
    return PrimaryKeyValue(string(id))
  }
}
