package com.hippo.ehviewer.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.hippo.ehviewer.R
import com.hippo.ehviewer.Settings
import com.hippo.ehviewer.ui.compose.observed
import com.hippo.ehviewer.ui.login.LocalNavController

@Composable
fun EhScreen() {
    val navController = LocalNavController.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_eh)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding()).nestedScroll(scrollBehavior.nestedScrollConnection).verticalScroll(rememberScrollState())) {
            Preference(
                title = stringResource(id = R.string.account_name),
                summary = stringResource(id = R.string.settings_eh_identity_cookies_tourist),
            )
            Preference(
                title = stringResource(id = R.string.image_limits),
            )
            SimpleMenuPreferenceInt(
                title = stringResource(id = R.string.settings_eh_gallery_site),
                entry = R.array.gallery_site_entries,
                entryValueRes = R.array.gallery_site_entry_values,
                value = Settings::gallerySite.observed,
            )
            Preference(
                title = stringResource(id = R.string.settings_u_config),
                summary = stringResource(id = R.string.settings_u_config_summary),
            )
            Preference(
                title = stringResource(id = R.string.settings_my_tags),
                summary = stringResource(id = R.string.settings_my_tags_summary),
            )
            SimpleMenuPreferenceInt(
                title = stringResource(id = rikka.core.R.string.dark_theme),
                entry = R.array.night_mode_entries,
                entryValueRes = R.array.night_mode_values,
                value = Settings::theme.observed,
            )
            SwitchPreference(
                title = stringResource(id = R.string.black_dark_theme),
                summary = null,
                value = Settings::blackDarkTheme,
            )
            SimpleMenuPreferenceInt(
                title = stringResource(id = R.string.settings_eh_launch_page),
                entry = R.array.launch_page_entries,
                entryValueRes = R.array.launch_page_entry_values,
                value = Settings::launchPage.observed,
            )
            SimpleMenuPreferenceInt(
                title = stringResource(id = R.string.settings_eh_list_mode),
                entry = R.array.list_mode_entries,
                entryValueRes = R.array.list_mode_entry_values,
                value = Settings::listMode.observed,
            )
            IntSliderPreference(
                maxValue = 60,
                minValue = 20,
                title = stringResource(id = R.string.list_tile_thumb_size),
                value = Settings::listThumbSize,
            )
            SimpleMenuPreferenceInt(
                title = stringResource(id = R.string.settings_eh_thumb_resolution),
                entry = R.array.thumb_resolution_entries,
                entryValueRes = R.array.thumb_resolution_entry_values,
                value = Settings::thumbResolution.observed,
            )
            SwitchPreference(
                title = stringResource(id = R.string.settings_eh_show_jpn_title),
                summary = stringResource(id = R.string.settings_eh_show_jpn_title_summary),
                value = Settings::showJpnTitle,
            )
            SwitchPreference(
                title = stringResource(id = R.string.settings_eh_show_gallery_pages),
                summary = stringResource(id = R.string.settings_eh_show_gallery_pages_summary),
                value = Settings::showGalleryPages,
            )
            SwitchPreference(
                title = stringResource(id = R.string.settings_eh_show_gallery_comments),
                summary = stringResource(id = R.string.settings_eh_show_gallery_comments_summary),
                value = Settings::showComments,
            )
            SwitchPreference(
                title = stringResource(id = R.string.settings_eh_show_tag_translations),
                summary = stringResource(id = R.string.settings_eh_show_tag_translations_summary),
                value = Settings::showTagTranslations,
            )
            UrlPreference(
                title = stringResource(id = R.string.settings_eh_tag_translations_source),
                url = stringResource(id = R.string.settings_eh_tag_translations_source_url),
            )
            Preference(
                title = stringResource(id = R.string.settings_eh_filter),
                summary = stringResource(id = R.string.settings_eh_filter_summary),
            )
            SwitchPreference(
                title = stringResource(id = R.string.settings_eh_metered_network_warning),
                summary = null,
                value = Settings::meteredNetworkWarning,
            )
            SwitchPreference(
                title = stringResource(id = R.string.settings_eh_request_news),
                summary = null,
                value = Settings::requestNews,
            )
            Preference(
                title = stringResource(id = R.string.settings_eh_request_news_timepicker),
                summary = null,
            )
            SwitchPreference(
                title = stringResource(id = R.string.settings_eh_hide_hv_events),
                summary = null,
                value = Settings::requestNews,
            )
            Spacer(modifier = Modifier.size(it.calculateBottomPadding()))
        }
    }
}