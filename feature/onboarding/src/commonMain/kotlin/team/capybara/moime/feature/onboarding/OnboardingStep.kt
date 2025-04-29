/*
 * Copyright 2025 Yeojun Yoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package team.capybara.moime.feature.onboarding

import moime.feature.onboarding.generated.resources.Res
import moime.feature.onboarding.generated.resources.img_onboarding_1
import moime.feature.onboarding.generated.resources.img_onboarding_2
import moime.feature.onboarding.generated.resources.img_onboarding_3
import moime.feature.onboarding.generated.resources.img_onboarding_4
import moime.feature.onboarding.generated.resources.onboarding_desc_1
import moime.feature.onboarding.generated.resources.onboarding_desc_2
import moime.feature.onboarding.generated.resources.onboarding_desc_3
import moime.feature.onboarding.generated.resources.onboarding_desc_4
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

internal enum class OnboardingStep(
    val textRes: StringResource,
    val imageRes: DrawableResource
) {
    Step1(
        textRes = Res.string.onboarding_desc_1,
        imageRes = Res.drawable.img_onboarding_1
    ),
    Step2(
        textRes = Res.string.onboarding_desc_2,
        imageRes = Res.drawable.img_onboarding_2
    ),
    Step3(
        textRes = Res.string.onboarding_desc_3,
        imageRes = Res.drawable.img_onboarding_3
    ),
    Step4(
        textRes = Res.string.onboarding_desc_4,
        imageRes = Res.drawable.img_onboarding_4
    );
}
