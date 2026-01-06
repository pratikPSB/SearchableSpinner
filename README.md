# SearchableSpinner

A custom spinner dialog with search functionality that allows for single selection.

## Features

- Search functionality in the spinner dialog.
- Single item selection.
- Customizable dialog title, search hint, and negative button text.
- Show/hide search bar.
- Show/hide selection tick.
- Set dialog cancelable.

## Installation

### Step 1: Add Jitpack repository

Add the JitPack repository to your `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    repositories {
        //...
        // other repositories
        //...
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add dependency

Add the dependency to your module-level `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("com.github.pratikPSB:SearchableSpinnerDialog:Tag")
}
```

Replace `Tag` with the latest release version.

## Usage

### 1. Add `SearchableSpinnerView` to your layout XML file:

```xml
<com.psb.searchablespinner.searchablespinner.SearchableSpinnerView
    android:id="@+id/spUser"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_margin="@dimen/_16sdp"
    android:background="@drawable/back_textview_spinner"
    android:drawableTint="@color/purple_700"
    android:padding="@dimen/_10sdp"
    android:textColor="@color/purple_700"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:setCancelable="true"
    app:showTick="true"
    app:dialogTitle="Select User"
    app:dialogSearchHint="Search here..."
    app:negativeButtonText="Cancel"
    tools:text="jjkhkj" />
```

### 2. In your Activity or Fragment, initialize the `SearchableSpinnerView`:

```kotlin
class SpinnerActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySpinnerBinding.inflate(layoutInflater) }
    private val userList = ArrayList<Spinner>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // ...

        setUserList()
    }

    private fun setUserList() {
        if (userList.isNotEmpty())
            userList.clear()

        userList.add(Spinner(0, "--Select--"))
        for (i in 1..26) {
            userList.add(Spinner(i, "User $i"))
        }

        setUserListAdapter()
    }

    private fun setUserListAdapter() {
        binding.spUser.setData(this@SpinnerActivity, userList, object : SpinnerListener {
            override fun setOnItemClickListener(position: Int) {
                Toast.makeText(this@SpinnerActivity, "${userList[position].id}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.spUser.setSelectedItemById(2)
    }
}
```

## Customization Attributes

You can customize the `SearchableSpinnerView` using the following attributes in your XML layout:

| Attribute            | Description                                       | Format    | Default Value    |
|----------------------|---------------------------------------------------|-----------|------------------|
| `showSearchBar`      | Show or hide the search bar in the dialog.        | `boolean` | `true`           |
| `setCancelable`      | Set whether the dialog is cancelable.             | `boolean` | `false`          |
| `showTick`           | Show or hide the tick icon for the selected item. | `boolean` | `false`          |
| `dialogTitle`        | Set the title of the dialog.                      | `string`  | "Select Item"    |
| `dialogSearchHint`   | Set the hint text for the search view.            | `string`  | "Search here..." |
| `negativeButtonText` | Set the text for the negative button.             | `string`  | "Close"          |

## Public Methods

- `setSelectedItemById(id: Int)`: Select an item by its ID.
- `setSelectedItemByName(name: String?)`: Select an item by its name.
- `getSelectedItemId(): Int`: Get the ID of the selected item.
- `getSelectedItemName(): String`: Get the name of the selected item.
- `getSelectedItemIndex(): Int`: Get the index of the selected item.
- `setPreviousSelectedItemIndex(index: Int)`: Set the previously selected item by its index.
- `dismiss()`: Dismiss the dialog.

