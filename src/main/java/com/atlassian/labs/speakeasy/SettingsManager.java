package com.atlassian.labs.speakeasy;

import com.atlassian.labs.speakeasy.data.SpeakeasyData;
import com.atlassian.labs.speakeasy.model.Settings;
import com.atlassian.labs.speakeasy.util.JsonObjectMapper;
import org.netbeans.lib.cvsclient.commandLine.command.log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class SettingsManager
{
    private final SpeakeasyData data;

    private volatile Settings settings;

    public SettingsManager(SpeakeasyData data)
    {
        this.data = data;
        setSettings(loadSettings());
    }

    public Settings getSettings()
    {
        return settings;
    }

    public Settings setSettings(Settings settings)
    {
        String value = null;
        try
        {
            value = JsonObjectMapper.write(settings);
            this.settings = JsonObjectMapper.read(Settings.class, data.saveSettings(value));
            return getSettings();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Cannot save settings due to error", e);
        }
    }

    private Settings loadSettings()
    {
        String value = data.getSettings();
        try
        {
            return JsonObjectMapper.read(Settings.class, value);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Cannot get settings", e);
        }
    }
}
