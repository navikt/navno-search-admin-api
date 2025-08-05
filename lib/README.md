# navno-search-admin-api Common Library

This library provides common models, repositories, and utilities for working with the navno search content index in OpenSearch.

## Usage

### For Applications with Admin Access (Default)

By default, the library is configured for applications with full admin access to OpenSearch. Simply include the dependency and the repositories will be automatically configured:

```kotlin
dependencies {
    implementation("no.nav.navnosearchadminapi:common:VERSION")
}
```

### For Applications with Read-Only Access

Applications that only have read permissions in OpenSearch need to configure the library for read-only mode to prevent initialization errors.

Add the following to your `application.yml`:

```yaml
opensearch:
  access-mode: read  # Enables read-only mode
  # Your other OpenSearch configuration...
```

In read-only mode:
- The `@Document` annotation's `createIndex` is set to `false` to prevent automatic index creation
- A custom read-only repository implementation is used that:
  - Allows all read operations (findById, existsById, count, findAllByTeamOwnedBy)
  - Throws `UnsupportedOperationException` for all write operations
  - Prevents any index management operations

### Accessing Index Information

The library provides the `IndexInfo` object for accessing index metadata without requiring OpenSearch operations:

```kotlin
import no.nav.navnosearchadminapi.common.constants.IndexInfo

// Get the index name
val indexName = IndexInfo.INDEX_NAME  // "search-content-v8"

// Get the index version
val version = IndexInfo.INDEX_VERSION  // 8
```

### Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `opensearch.access-mode` | The access mode: "admin" or "read" | "admin" |
| `opensearch.repository.enabled` | Whether to enable automatic repository configuration | true |

### Important Notes

1. The index name includes version information (e.g., "search-content-v8")
2. After moving to the 'navno' namespace in Nais, versions v1-v7 no longer exist
3. The library maintains version continuity despite the infrastructure change
4. Read-only applications should handle `UnsupportedOperationException` for write operations