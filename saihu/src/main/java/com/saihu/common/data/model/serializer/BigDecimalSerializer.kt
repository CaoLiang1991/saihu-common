package com.saihu.common.data.model.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = BigDecimal::class)
object BigDecimalSerializer : KSerializer<BigDecimal> {
    override fun serialize(encoder: Encoder, value: BigDecimal) {
        // 在序列化时移除尾随零
        encoder.encodeString(value.stripTrailingZeros().toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        // 在反序列化时也可以应用stripTrailingZeros
        return BigDecimal(decoder.decodeString()).stripTrailingZeros().toPlainString()
            .toBigDecimal()
    }
}