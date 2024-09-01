package org.http4k.intellij.wizard

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import org.http4k.format.Jackson
import org.http4k.intellij.wizard.Step.Choice
import org.http4k.intellij.wizard.Step.Input
import org.http4k.intellij.wizard.Step.MultiChoice
import org.http4k.intellij.wizard.Step.Section

data class Option(val label: String, val description: String?, val default: Boolean, val steps: List<Step>)

@JsonTypeInfo(use = Id.NAME, property = "stepType")
@JsonSubTypes(
    Type(value = Section::class, name = "section"),
    Type(value = Input::class, name = "input"),
    Type(value = Choice::class, name = "choice"),
    Type(value = MultiChoice::class, name = "multi")
)

sealed class Step(val label: String) {
    override fun toString() = Jackson.compactify(Jackson.asFormatString(this))

    class Section(label: String, val steps: List<Step>) : Step(label)
    class Input(label: String, val default: String) : Step(label)

    class Choice(label: String, val options: List<Option>) : Step(label)
    class MultiChoice(label: String, val options: List<Option>) : Step(label)
}

data class Questionnaire(val steps: List<Step>)

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes(
    Type(value = Answer.Text::class, name = "text"),
    Type(value = Answer.Step::class, name = "step")
)
sealed class Answer {
    abstract val steps: List<Answer>
    abstract val label: String

    data class Text(
        override val label: String,
        val answers: List<String> = emptyList(),
        override val steps: List<Answer> = emptyList()
    ) : Answer()

    data class Step(
        override val label: String,
        val answers: List<Answer> = emptyList(),
        override val steps: List<Answer> = emptyList()
    ) : Answer()
}
