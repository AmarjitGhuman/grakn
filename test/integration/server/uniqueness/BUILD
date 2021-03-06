#
# Copyright (C) 2020 Grakn Labs
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

load("@graknlabs_dependencies//tool/checkstyle:rules.bzl", "checkstyle_test")

java_test(
    name = "attribute-uniqueness-it",
    size = "large",
    srcs = ["AttributeUniquenessIT.java"],
    classpath_resources = ["//test/resources:logback-test"],
    test_class = "grakn.core.server.uniqueness.AttributeUniquenessIT",
    deps = [
        "//concept/answer",
        "//core",
        "//kb/server",
        "//kb/concept/api",
        "//test/rule:grakn-test-server",
        "@graknlabs_graql//java:graql",
        "@graknlabs_common//:common",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_hamcrest_hamcrest_library"
    ],
)

java_test(
    name = "concurrency-it",
    size = "enormous",
    srcs = [
        "ConcurrencyIT.java",
        "element/AttributeElement.java",
        "element/Element.java",
        "element/Record.java"
    ],
    classpath_resources = ["//test/resources:logback-test"],
    data = ["//:assemble-mac-zip"],
    test_class = "grakn.core.server.uniqueness.ConcurrencyIT",
    deps = [
        "//concept/answer",
        "//kb/concept/api",
        "//kb/server",
        "//test/rule:grakn-test-server",
        "@graknlabs_common//:common",
        "@graknlabs_graql//java:graql",
        "@maven//:commons_io_commons_io",
        "@maven//:commons_lang_commons_lang",
        "@maven//:com_google_guava_guava",
    ],
)

checkstyle_test(
    name = "checkstyle",
    targets = [
        ":attribute-uniqueness-it",
        ":concurrency-it"
    ],
)
