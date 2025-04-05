## TabooButton
`TabooButton`은 사용자가 클릭할 수 있는 기본적인 버튼입니다. `TabooButton`은 다양한 속성을 가지고 있어 다양한 스타일의 버튼을 만들 수 있습니다.

![TabooButton](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_button.png)

### `buttonShape` 속성
`buttonShape` 속성은 버튼의 모양을 결정합니다. `buttonShape` 속성은 다음과 같은 값들을 가질 수 있습니다.
- `rectangle`: 직사각형 모양의 버튼 (기본값)
- `round`: 둥근 모양의 버튼

### `buttonType` 속성
`buttonType` 속성은 모양을 제외한 전반적인 스타일을 결정하는 속성입니다. 기본값은 `solid` 입니다.

`buttonType` 속성은 다음과 같은 값들을 가질 수 있습니다.


| Attr      | Text Color  | Background Color | Stroke Type  | Stroke Color  |
|-----------|-------------|------------------|--------------|---------------|
| `solid`   | White       | Primary Color    | Solid        | Primary Color |
| `pill`    | Primary     | Secondary Color  | Solid        | Primary Color |
| `outline` | Primary     | Transparent      | Solid        | Primary Color |
| `dash`    | Primary     | Transparent      | Dashed       | Primary Color |


## 사용하기
`TabooButton`을 사용하기 위해서 다음과 같이 레이아웃 파일에 추가합니다.

### 기본 사용 예제
```xml
<com.kwon.taboo.button.TabooButton
    android:id="@+id/btn_solid"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Solid Button"/>
```

### 속성 사용 예제
아래 예제는 버튼의 모양과 스타일을 지정하는 예제입니다.

![TabooButton](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_button_shape_round_type_solid.png)

```xml
<com.kwon.taboo.button.TabooButton
    android:id="@+id/btn_solid"
    android:layout_width="match_parent"
    android:layout_height="60dp"
    android:text="확인"
    app:buttonShape="round"
    app:buttonType="solid"/>
```

## 구성 및 주요 속성

### Text 속성들
| 요소        | 속성                  | 관련 메서드                                               | 기본값                                                                                     |
|-----------|---------------------|------------------------------------------------------|-----------------------------------------------------------------------------------------|
| **Text**  | `android:text`      | `setText(String)`<br/>`getText()`                    | ` `                                                                                     |
| **Color** | `android:textColor` | `setTextColor(ColorStateList?)`<br/>`getTextColor()` | `buttonTye`이 `solid`이면 `R.color.white`<br/>이며 그 외에는 `R.color.taboo_blue_900` 이다. |

### Button 속성들
| 요소                  | 속성                   | 관련 메서드                                      | 기본값                                 |
|---------------------|----------------------|---------------------------------------------|-------------------------------------|
| **Shape**           | `app:buttonShape`    | `setButtonShape`<br/>`getButtonShape`       | `rect`                              |
| **Type**            | `app:buttonType`     | `setButtonType`<br/>`getButtonType`         | `solid`                             |
| **Primary Color**   | `app:primaryColor`   | `setPrimaryColor`<br/>`getPrimaryColor`     | `R.color.taboo_blue_900`     |
| **Secondary Color** | `app:secondaryColor` | `setSecondaryColor`<br/>`getSecondaryColor` | `R.color.taboo_blue_100`             |
| **Ripple Color**    | `app:rippleColor`    | `setRippleColor`<br/>`getRippleColor`       | `R.color.taboo_button_ripple_color` |

### Icon 속성들
| 요소           | 속성                 | 관련 메서드                          | 기본값    |
|--------------|--------------------|---------------------------------|--------|
| **Icon**     | `app:icon`         | `setIcon`<br/>`getIcon`         | `null` |
| **Position** | `app:iconPosition` | `setIcon`<br/>`getIconPosition` | `left` |