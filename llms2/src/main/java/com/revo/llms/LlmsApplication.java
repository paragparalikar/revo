package com.revo.llms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@PWA(name = "Line Loss Monitoring System", shortName = "LLMS", offlineResources = {})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class LlmsApplication extends SpringBootServletInitializer implements AppShellConfigurator {
	private static final long serialVersionUID = -5170116799200123108L;

	public static void main(String[] args) {
        SpringApplication.run(LlmsApplication.class, args);
    }

}
