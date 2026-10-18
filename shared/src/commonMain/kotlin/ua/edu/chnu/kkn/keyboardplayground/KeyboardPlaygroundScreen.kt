package ua.edu.chnu.kkn.keyboardplayground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A sandbox screen showing how [KeyboardOptions] pick the soft keyboard layout and IME button,
 * and how [KeyboardActions] react when that button is pressed.
 */
@Composable
fun KeyboardPlaygroundScreen(modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val values = remember { mutableStateMapOf<String, String>() }
    var lastAction by remember { mutableStateOf("Nothing yet. Focus a field and press the keyboard's action key.") }

    // Next/Previous: move focus explicitly instead of relying on the default behavior.
    val next = KeyboardActions(onNext = {
        lastAction = "Next → focus moved down"
        focusManager.moveFocus(FocusDirection.Next)
    })
    val previous = KeyboardActions(onPrevious = {
        lastAction = "Previous → focus moved up"
        focusManager.moveFocus(FocusDirection.Previous)
    })
    // Done: finish editing by hiding the keyboard and clearing focus.
    val done = KeyboardActions(onDone = {
        lastAction = "Done → keyboard hidden, focus cleared"
        keyboard?.hide()
        focusManager.clearFocus()
    })

    @Composable
    fun Field(
        label: String,
        caption: String,
        options: KeyboardOptions,
        actions: KeyboardActions,
        singleLine: Boolean = true,
        visualTransformation: VisualTransformation = VisualTransformation.None,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LabeledField(
                label = label,
                value = values[label].orEmpty(),
                onValueChange = { values[label] = it },
                keyboardOptions = options,
                keyboardActions = actions,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Keyboard playground",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {

            LastAction(lastAction)

            Section("Keyboard types")
            Field(
                label = "Text",
                caption = "KeyboardType.Text · Capitalization.Sentences · ImeAction.Next",
                options = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                actions = next,
            )
            Field(
                label = "Email",
                caption = "KeyboardType.Email · ImeAction.Next",
                options = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                actions = next,
            )
            Field(
                label = "Phone",
                caption = "KeyboardType.Phone · ImeAction.Next",
                options = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                actions = next,
            )
            Field(
                label = "Number",
                caption = "KeyboardType.Number · ImeAction.Next",
                options = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                actions = next,
            )
            Field(
                label = "Decimal",
                caption = "KeyboardType.Decimal · ImeAction.Next",
                options = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                actions = next,
            )
            Field(
                label = "Password",
                caption = "KeyboardType.Password · PasswordVisualTransformation · ImeAction.Next",
                options = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                actions = next,
                visualTransformation = PasswordVisualTransformation(),
            )
            Field(
                label = "PIN",
                caption = "KeyboardType.NumberPassword · ImeAction.Done",
                options = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                actions = done,
                visualTransformation = PasswordVisualTransformation(),
            )

            Section("Capitalization & autocorrect")
            Field(
                label = "Author name",
                caption = "Capitalization.Words · ImeAction.Next",
                options = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                actions = next,
            )
            Field(
                label = "Invite code",
                caption = "Capitalization.Characters · autoCorrectEnabled = false · ImeAction.Previous",
                options = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Previous,
                ),
                actions = previous,
            )

            Section("Keyboard actions")
            Field(
                label = "Website",
                caption = "KeyboardType.Uri · ImeAction.Go → pretends to open the link",
                options = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Go),
                actions = KeyboardActions(onGo = {
                    lastAction = "Go → opening ${values["Website"].orEmpty().ifBlank { "(empty URL)" }}"
                }),
            )
            Field(
                label = "Search quotes",
                caption = "ImeAction.Search → runs the search and hides the keyboard",
                options = KeyboardOptions(imeAction = ImeAction.Search),
                actions = KeyboardActions(onSearch = {
                    lastAction = "Search → looking for “${values["Search quotes"].orEmpty()}”"
                    keyboard?.hide()
                }),
            )
            Field(
                label = "Message",
                caption = "ImeAction.Send → sends and clears the field",
                options = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Send),
                actions = KeyboardActions(onSend = {
                    lastAction = "Send → sent “${values["Message"].orEmpty()}”"
                    values["Message"] = ""
                }),
            )
            Field(
                label = "Note",
                caption = "Multi-line · ImeAction.Default → the action key inserts a new line",
                options = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Default),
                actions = KeyboardActions.Default,
                singleLine = false,
            )
            Field(
                label = "Comment",
                caption = "ImeAction.Done → hides the keyboard and clears focus",
                options = KeyboardOptions(imeAction = ImeAction.Done),
                actions = done,
            )
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessage: String? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val isError = errorMessage != null
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.5.sp, letterSpacing = 1.5.sp),
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 3,
            textStyle = MaterialTheme.typography.bodyMedium,
            trailingIcon = trailing,
            isError = isError,
            supportingText = errorMessage?.let { { Text(it) } },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}
@Composable
private fun Section(title: String) {
    Text(
        text = title.uppercase(),
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 11.sp, letterSpacing = 2.4.sp),
        color = MaterialTheme.colorScheme.outline,
    )
}

@Composable
private fun LastAction(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "LAST KEYBOARD ACTION",
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.5.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
