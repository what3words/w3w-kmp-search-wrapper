package com.what3words.search.wrapper.core.language

import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WRFC5646Language

internal fun W3WLanguage.toIEFTCode(): String {
    return when (this) {
        W3WRFC5646Language.BS_LATN, W3WRFC5646Language.BS_CYRL -> "bs"
        W3WRFC5646Language.HR -> "hr"
        W3WRFC5646Language.SR_LATN_RS, W3WRFC5646Language.SR_CYRL_RS,
        W3WRFC5646Language.SR_LATN_ME, W3WRFC5646Language.SR_CYRL_ME -> "sr"
        else -> this.w3wCode
    }
}
