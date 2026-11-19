package eu.karcags.mythscape.utils

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.and

class QueryWhereBuilder {

    private val operands = mutableListOf<Op<Boolean>>()

    fun add(operand: Op<Boolean>): QueryWhereBuilder {
        operands.add(operand)
        return this
    }

    fun build(): Op<Boolean> {
        if (operands.isEmpty()) {
            return Op.TRUE
        }

        if (operands.size == 1) {
            return operands[0]
        }

        return operands.reduce { ac, op ->
            ac and op
        }
    }
}

fun queryWhere(builder: QueryWhereBuilder.() -> Unit): Op<Boolean> {
    return QueryWhereBuilder().apply(builder).build()
}