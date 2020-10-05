package com.excentria_it.wamya;

import com.excentria_it.wamya.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

class DependencyRuleTests {

	@Test
	void validateRegistrationContextArchitecture() {
		HexagonalArchitecture.boundedContext("com.codisiac.wamya")

				.withDomainLayer("domain")

				.withAdaptersLayer("adapter")
				.incoming("web")
				.outgoing("persistence")
				.and()

				.withApplicationLayer("application")
				.services("service")
				.incomingPorts("port.in")
				.outgoingPorts("port.out")
				.and()

				.withConfiguration("configuration")
				.check(new ClassFileImporter()
						.importPackages("com.codisiac.wamya.."));
	}

	@Test
	void testPackageDependencies() {
		noClasses()
				.that()
				.resideInAPackage("com.codisiac.wamya.domain..")
				.should()
				.dependOnClassesThat()
				.resideInAnyPackage("com.codisiac.wamya.application..")
				.check(new ClassFileImporter()
						.importPackages("com.codisiac.wamya.."));
	}

}
