import SwiftUI
import shared


func carScreen(component: AppComponent) -> some View{
  typealias ObservableCarScreen = ObservableStore<CarScreenStore, CarScreenModel, CarScreenEvents, CarScreenEffects>
  let obsStore: ObservableCarScreen
  let carScreenComponent = InjectCarScreenComponent(parent: component)
  
  let store = carScreenComponent.carScreenStore
  let effectHandler = carScreenComponent.carScreenEffectHandler
  obsStore = ObservableCarScreen(
    store: store,
    state: CarScreenModel.Companion.shared.defaultModel(),
    stateWatcher: store.watchState(),
    sideEffectWatcher: store.watchSideEffect()
  )
  
  _ = Loop<CarScreenModel, CarScreenEvents, CarScreenEffects, CarScreenEffectHandler>(
    store: store,
    effectHandler: effectHandler,
    startEffects: {
      let initEffects : Set = [CarScreenEffects.LoadInitialData.shared]
      return initEffects
    },
    eventSources: [Flow]())
  
  return CarScreen().environmentObject(obsStore)
}

struct CarScreen: ConnectedView {
  typealias S = CarScreenStore
  typealias M = CarScreenModel
  typealias E = CarScreenEvents
  typealias F = CarScreenEffects
  
  struct Props {
    let addCarMode: Bool
    let cars: [UiCar]
    let selectableYears: [Int]
    let onAddCar: () -> Void
    let onDiscardCreateCar: () -> Void
    let onCreateCar: (Int, String, String, String?, String?) -> Void
  }
  
  func map(state: CarScreenModel, dispatch: @escaping DispatchFunction<CarScreenEvents>) -> Props {
    return Props(
      addCarMode: state.addCarMode,
      cars: state.cars,
      selectableYears: state.selectableYears.map({ kInt in
        kInt.intValue
      }),
      onAddCar: { dispatch(CarScreenEvents.AddCar.shared)},
      onDiscardCreateCar: { dispatch(CarScreenEvents.DismissAddCarDialog.shared)},
      onCreateCar: { year, make, model, trim, nickname in
        dispatch(CarScreenEvents.CreateCar(
          year: Int32(year), make: make, model: model, trim: trim, nickname: nickname
        ))
      }
    )
  }
  
  func body(props: Props) -> some View {
    NavigationView {
      VStack {
        if (props.addCarMode) {
          AddCarView(selectableYears: props.selectableYears, onDiscard: props.onDiscardCreateCar, onSubmit: props.onCreateCar)
        }
        else {
          if (props.cars.isEmpty) {
            addCarButton(onAddCar: props.onAddCar)
          }
          else {
            ScrollView {
              LazyVStack(alignment: .leading) {
                ForEach(props.cars, id: \.self) { car in
                  Text("\(car.titleDisplay())")
                    .font(.title3)
                  if let nickname = car.nickname {
                    Text(nickname)
                      .font(.subheadline)
                  }
                }
              }
            }
            addCarButton(onAddCar: props.onAddCar)
            
          }
        }
      }
      .navigationTitle("Track Tracker")
    }
  }
  
  func addCarButton(onAddCar: @escaping () -> Void) -> some View {
    Button("Add a car") {
      onAddCar()
    }
  }
}

struct AddCarView: View {
  @State private var year: Int
  @State private var make: String = ""
  @State private var model: String = ""
  @State private var trim: String = ""
  @State private var nickname: String = ""
  
  let onDiscard: () -> Void
  let onSubmit: (Int, String, String, String?, String?) -> Void
  let selectableYears: [Int]
  
  init(selectableYears: [Int], onDiscard: @escaping () -> Void, onSubmit: @escaping (Int, String, String, String?, String?) -> Void ) {
    self.selectableYears = selectableYears
    self.year = self.selectableYears.first ?? 2021
    self.onDiscard = onDiscard
    self.onSubmit = onSubmit
  }
  
  var body: some View {
    VStack {
      Form {
        Picker("Year", selection: $year) {
          ForEach(selectableYears, id: \.self) {
            Text(String($0))
          }
        }
        .pickerStyle(DefaultPickerStyle())
        TextField("Make", text: $make)
        TextField("Model", text: $model)
        TextField("trim", text: $trim)
        TextField("nickname", text: $nickname)
      }
      HStack(spacing: 24) {
        Button("Discard") {
          onDiscard()
        }
        Button("Submit") {
          onSubmit(year, make, model, trim, nickname)
        }
      }
    }
  }
}
