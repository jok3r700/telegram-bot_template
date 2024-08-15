package com.example.conversation.controller

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.InputChain
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.generated.userData
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.BreakCondition
import eu.vendeli.tgbot.types.internal.ChainLink
import eu.vendeli.tgbot.types.internal.ProcessedUpdate

@InputChain
class ConversationChain {
    object Name : ChainLink() {
        override val breakCondition = BreakCondition { _, update, _ -> update.text.isBlank() }
        override val retryAfterBreak: Boolean = true
        override suspend fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {
            message {
                "Please say your name, because that's what well-mannered people do :)"
            }.send(user, bot)
        }

        override suspend fun action(user: User, update: ProcessedUpdate, bot: TelegramBot) {
            bot.userData[user, "name"] = update.text

            message { "Oh, ${update.text}, nice to meet you!" }
            message { "How old are you?" }.send(user, bot)
        }
    }

    object Age : ChainLink() {
        override val retryAfterBreak = true
        override val breakCondition = BreakCondition { _, update, _ -> update.text.toIntOrNull() == null }
        override suspend fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {
            message {
                "Perhaps it's not nice to ask your age, but maybe you can tell me anyway."
            }.send(user, bot)
        }

        override suspend fun action(user: User, update: ProcessedUpdate, bot: TelegramBot) {
            val name = bot.userData[user, "name"]
            message {
                "I'm not good at remembering, but I remembered you! " +
                        "You're $name and you're ${update.text} years old."
            }.send(user, bot)
        }
    }

}