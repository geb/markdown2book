# Preface

## About the Documentation

The intended purpose of the documentation is to give an understanding of the main features offered by TextMate and should provide you with those details which are not easily found alone by trial-and error. The documentation is not exhaustive.

You are supposed to already know what a text editor is, in particular have some experience with Cocoa's text editor control (used e.g. in TextEdit, Mail, and Xcode). While TextMate does not use that control, it does for the most part mimic its behavior.

If you want to print this documentation then [here is a printable version](all_pages.html).


## Philosophy of TextMate

From UNIX we get that _Tasks and Trends Change._ In concrete terms this means that instead of writing a command (in UNIX) to solve the problem at hand, we find the underlying pattern, write a command to solve types of that problem, and then use it in a script.

This gives us a command which we can re-use in the future for many problems of the same type, and since it‍ is generally much easier (and more flexible) to piece together a script of different commands, than it is to write a specific command for the task, this is a big gain in productivity. Especially since we actually do not write the command in the first place, we use an existing command that has already been written for this type of problem.

There are two ways in which TextMate leverages that philosophy. First it has good shell integration, so if you are skilled in using the UNIX shell, you should love TextMate for this integration.

But more ambitiously, TextMate tries to find the underlying patterns behind automating the tedious, being smart about what to do, and then provide you with the functionality so that you can combine it to settle your particular needs.

Granted, TextMate is not the first text editor which tries to be broad, but from Apple we get the venerable _Keep It Simple._ So even users with no or little experience with scripting and regular expressions are able to customize TextMate in ways that no other editor would have allowed them to.

That said, the philosophy of TextMate is also to _Educate the User._ So to fully capitalize on what TextMate gives you, you should learn a scripting language, [regular expressions][RegularExpressions], and should understand TextMates [scope][ScopeSelectors] and [snippet][Snippets] system (to some degree also [language grammars][LanguageGrammars]) and have an idea about the [shell infrastructure][ShellCommands] provided (in particular [environment variables][EnvVariables], pipes, and stdin/stdout).


## Terminology

For the most part TextMate and this documentation abides by [Apples terminology](http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGText/chapter_13_section_3.html#//apple_ref/doc/uid/TP30000365-TPXREF101). Below is a table of terms that might be a source of misunderstandings.

<table class="graybox" border="0" cellspacing="0" cellpadding="5">
    <tr>
        <th>Term</th>
        <th>Explanation</th>
    </tr>

    <tr>
        <td><p>Caret</p></td>
        <td><p>The text insertion point.</p></td>
    </tr>

    <tr>
        <td><p>Cursor</p></td>
        <td><p>Mouse pointer.</p></td>
    </tr>

    <tr>
        <td><p>Document</p></td>
        <td><p>This refers to a file when it is loaded into TextMate (for the purpose of being edited). Old-timers often refer to this as the <em>buffer</em>.</p></td>
    </tr>

    <tr>
        <td><p>Directory</p></td>
        <td><p>This is sometimes used instead of folder. Mainly folder is used when talking about the GUI and directory is used when talking about shell related things.</p></td>
    </tr>
</table>

Generally TextMate and this documentation use the glyph representation of a key. Below is a table with most glyphs, the name of the key (as used in this documentation) and a short explanation.



If you are unsure of the location of a key, you can bring up the Keyboard Viewer, which you can add to the Input menu in the International pane of System Preferences.

<table class="graybox" border="0" cellspacing="0" cellpadding="5">
    <tr>
        <th>Glyph</th>
        <th>Key Name</th>
        <th>Explanation</th>
    </tr>

    <tr>
        <td><p>&#x2303;</p></td>
        <td><p>Control</p></td>
        <td><p>This key is generally in the lower left corner of the keyboard (and symmetrically placed in the right side). In addition to key equivalents, this key is also used with a mouse click to bring up context sensitive menus.</p></td>
    </tr>

    <tr>
        <td><p>&#x2325;</p></td>
        <td><p>Option</p></td>
        <td><p>This is next to the control key and often bears the label Alt. If you hold down the option key while selecting text with the mouse, the selection will be rectangular. It is also possible to single-click with the option modifier down to place the caret unrestrained of line-endings. Together with shift, it does a (rectangular) selection to where you click.</p></td>
    </tr>

    <tr>
        <td><p>&#x2318;</p></td>
        <td><p>Command</p></td>
        <td><p>The command key is also referred to as the Apple key since it has an apple symbol on it (&#63743;).</p></td>
    </tr>

    <tr>
        <td><p>&#x21E7;</p></td>
        <td><p>Shift</p></td>
        <td><p>The shift key should be well-known. When used together with a mouse click, it extends the selection.</p></td>
    </tr>

    <tr>
        <td><p>&#x238B;</p></td>
        <td><p>Escape</p></td>
        <td><p>The escape key is generally in the upper left corner of the keyboard. This key can be used to dismiss (cancel) panels, which means dialogs and some (but not all) windows. In TextMate it‍ is also used to cycle through completion candidates.</p></td>
    </tr>

    <tr>
        <td><p>&#x2305;</p></td>
        <td><p>Enter</p></td>
        <td><p>The enter key is on the numeric keypad (and is not the same as return). On laptops it‍ is fn-return.</p></td>
    </tr>

    <tr>
        <td><p>&#x21A9;</p></td>
        <td><p>Return</p></td>
        <td><p>The return key should be well known.</p></td>
    </tr>

    <tr>
        <td><p>&#x2326;</p></td>
        <td><p>Forward&nbsp;Delete</p></td>
        <td><p>This is often just called delete and on the keyboard has a label of Del or Delete.</p></td>
    </tr>

    <tr>
        <td><p>&#x232B;</p></td>
        <td><p>Backward&nbsp;Delete</p></td>
        <td><p>Often called backspace. On my keyboard this just has a left pointing arrow on the key (&#x2190;).</p></td>
    </tr>

    <tr>
        <td><p>&#xFE56;&#x20DD;</p></td>
        <td><p>Help</p></td>
        <td><p>The Help key is located above forward delete, but not all keyboards have it. Generally it has the word Help on the key, but it has been known as the Ins key as well.</p></td>
    </tr>

    <tr>
        <td><p>&#x2196;</p></td>
        <td><p>Home</p></td>
        <td><p>The Home key scrolls to the beginning of the document, but does not move the caret.</p></td>
    </tr>

    <tr>
        <td><p>&#x2198;</p></td>
        <td><p>End</p></td>
        <td><p>The End key scrolls to the end of the document, but does not move the caret.</p></td>
    </tr>

    <tr>
        <td><p>&#x21DE;</p></td>
        <td><p>Page&nbsp;Up</p></td>
        <td><p>Scrolls up a page, but does not move the caret. If used with the option modifier it will scroll the caret. Likewise will shift cause a selection.</p></td>
    </tr>

    <tr>
        <td><p>&#x21DF;</p></td>
        <td><p>Page&nbsp;Down</p></td>
        <td><p>Scroll down a page without moving the caret. Using the option key will cause the caret to be moved.</p></td>
    </tr>

    <tr>
        <td><p>&#x21E5;</p></td>
        <td><p>Tab</p></td>
        <td><p>The tab key is used to insert a tab character (or equivalent amount of spaces if soft tabs is enabled). In normal controls it advances the focus to the next control.</p></td>
    </tr>

    <tr>
        <td><p>&#x21E4;</p></td>
        <td><p>Back-tab</p></td>
        <td><p>The back-tab key is achieved by holding down shift while using the normal tab key.</p></td>
    </tr>
</table>

## Limitations

TextMate is work-in-progress. One current key limitation (for non-Western users) is support for international input modes (e.g. CJK), proportional fonts, right-to-left text rendering and other (UniCode) features. As the author, I do understand the desire from users to have TextMate support these things, but currently proper support for this is a long-term to-do item.

And on the topic of limitations, I am also aware of the desire for (s)ftp integration, code hinting, split views, better printing, indented soft wrap, coffee making, and literally hundreds of other user requests. You will be able to find my comments on most feature requests by [searching the mailing list][MailingListSearch] archive, but I do not give estimates or timeframes, other than what version number I plan for something to appear in.