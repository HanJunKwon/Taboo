## TabooMenuButton
`TabooMenuButton`은 메뉴 버튼을 표현하는 컴포넌트입니다.

![TabooMenuButton](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_menu_button_none.png)

### `menuType` 속성
`menuType` 속성은 메뉴 버튼에서 제공되는 기능과 디자인을 결정합니다. 
`menuType` 속성은 다음과 같은 값들을 가질 수 있습니다.
- `none`: 메뉴명과 메뉴설명만 표시 버튼. (기본값)
- `preview`: 우측에 현재 설정된 메뉴를 미리보기로 표시하는 버튼.
- `toggle`: 우측에 현재 설정된 메뉴를 토글로 표시하는 버튼.

## 사용하기
`TabooMenuButton`을 사용하기 위해서 다음과 같이 레이아웃 파일에 추가합니다.

### 기본 사용 예제

아래 예제는 `menuType`을 `none`으로 지정한 예제입니다.

![TabooMenuButton_MenuType_None](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_menu_button_none.png)

```xml
<com.kwon.taboo.button.TabooMenuButton
    android:id="@+id/tmb_info"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:menuTitle="내 정보"
    app:menuDescription="내 정보로 이동합니다."/>
```

### 속성 사용 예제

#### `menuType` 속성 사용 예제
아래 예제는 `menuType`을 `preview`로 지정한 예제입니다.

![TabooMenuButton_MenuType_Preview](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_menu_button_preview.png)

```xml
<com.kwon.taboo.button.TabooMenuButton
    android:id="@+id/tmb_language"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:menuTitle="언어 변경"
    app:menuDescription="언어를 변경합니다."
    app:menuType="preview"
    app:previewGravity="center"
    app:preview="한국어"/>
```

아래 예제는 `menuType`을 `toggle`로 지정한 예제입니다.

![TabooMenuButton_MenuType_Preview](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_menu_button_toggle.png)

```xml
<com.kwon.taboo.button.TabooMenuButton
    android:id="@+id/tmb_theme"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:menuTitle="다크 테마"
    app:menuDescription="화면을 어둡게 변경합니다."
    app:menuType="toggle"
    app:isChecked="true"/>
```

## 구성 및 주요 속성

### Text 속성들
| 요소              | 속성                | 관련 메서드                   | 기본값                |
|-----------------|-------------------|--------------------------|--------------------|
| **Text**        | `android:text`    | `setText(String)`        | `Menu Title`       |
| **Description** | `app:description` | `setDescription(String)` | `Menu Description` |

### Type 속성들
| 요소                  | 속성                   | 관련 메서드              | 기본값        |
|---------------------|----------------------|---------------------|------------|
| **Type**            | `app:menuType`       | `setMenuType`       | `none`     |
| **Preview**         | `app:preview`        | `setPreview`        | `Preview`  |
| **Preview Gravity** | `app:previewGravity` | `setPreviewGravity` | `top`      |
| **checked**         |                      | `setToggleChecked`  | `false`    |

### Icon 속성들
| 요소                   | 속성                 | 관련 메서드              | 기본값 |
|----------------------|--------------------|---------------------|-----|
| **Icon Resource Id** | `app:icon`         | `setIconResourceId` | `0` |