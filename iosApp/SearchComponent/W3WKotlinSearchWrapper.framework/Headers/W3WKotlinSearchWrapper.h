#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class WWKSW__SkieTypeExportsKt, WWKSW__SkieSuspendWrappersKt, WWKSWW3WVoiceDataSourceVersion, WWKSWW3WTextDataSourceVersion, WWKSWW3WSuggestionCompanion, WWKSWW3WSuggestion, WWKSWW3WSearchClientConfig, WWKSWW3WSearchClient, WWKSWW3WResultSuccess<T>, WWKSWW3WResultFailure<T>, WWKSWW3WResult<T>, WWKSWW3WRectangleCompanion, WWKSWW3WRectangle, WWKSWW3WRFC5646LanguageCompanion, WWKSWW3WRFC5646Language, WWKSWW3WProprietaryLanguageCompanion, WWKSWW3WProprietaryLanguage, WWKSWW3WPolygonCompanion, WWKSWW3WPolygon, WWKSWW3WMicrophoneCompanion, WWKSWW3WMicrophone, WWKSWW3WLineCompanion, WWKSWW3WLine, WWKSWW3WLanguageCompanion, WWKSWW3WImage, WWKSWW3WIOSAudioStreamEncoding, WWKSWW3WGridSectionCompanion, WWKSWW3WGridSection, WWKSWW3WError, WWKSWW3WDistanceCompanion, WWKSWW3WDistance, WWKSWW3WCountryCompanion, WWKSWW3WCountry, WWKSWW3WCoordinatesCompanion, WWKSWW3WCoordinates, WWKSWW3WCircleCompanion, WWKSWW3WCircle, WWKSWW3WAutosuggestOptionsCompanion, WWKSWW3WAutosuggestOptionsBuilder, WWKSWW3WAutosuggestOptions, WWKSWW3WAutosuggestInputType, WWKSWW3WAudioStreamState, WWKSWW3WAudioStreamProxy, WWKSWW3WAudioStreamConfig, WWKSWW3WAudioStream, WWKSWW3WAddressCompanion, WWKSWW3WAddress, WWKSWUi_graphicsColorSpace, WWKSWUShort, WWKSWULong, WWKSWUInt, WWKSWUByte, WWKSWThreeWordAddressSearchProviderKt, WWKSWThreeWordAddressSearchConfig, WWKSWThreeWordAddressSearch, WWKSWSkie_SuspendResultSuccess, WWKSWSkie_SuspendResultError, WWKSWSkie_SuspendResultCanceled, WWKSWSkie_SuspendResult, WWKSWSkie_SuspendHandler, WWKSWSkie_CancellationHandler, WWKSWSkieKotlinStateFlow<T>, WWKSWSkieKotlinSharedFlow<T>, WWKSWSkieKotlinOptionalStateFlow<T>, WWKSWSkieKotlinOptionalSharedFlow<T>, WWKSWSkieKotlinOptionalMutableStateFlow<T>, WWKSWSkieKotlinOptionalMutableSharedFlow<T>, WWKSWSkieKotlinOptionalFlow<T>, WWKSWSkieKotlinMutableStateFlow<T>, WWKSWSkieKotlinMutableSharedFlow<T>, WWKSWSkieKotlinFlow<T>, WWKSWSkieColdFlowIterator<E>, WWKSWSimpleSearchPlugin<TConfig, TProvider>, WWKSWShort, WWKSWSearchResultSearchSuggestion, WWKSWSearchResultResolvedAddress, WWKSWSearchResultCompanion, WWKSWSearchResult, WWKSWSearchPluginHandle, WWKSWSearchPlugin<TConfig, TProvider>, WWKSWSearchConfig, WWKSWProviderNotResolvableException, WWKSWProviderNotFoundException, WWKSWNumber, WWKSWMutableSet<ObjectType>, WWKSWMutableDictionary<KeyType, ObjectType>, WWKSWMissingSuggestionTitleException, WWKSWMayBeAThreeWordAddressSearchProviderKt, WWKSWMayBeAThreeWordAddressSearchConfig, WWKSWMayBeAThreeWordAddressSearch, WWKSWLong, WWKSWLocationBiasRectangle, WWKSWLocationBiasCircle, WWKSWLocationBias, WWKSWKotlinx_serialization_coreStructureKindOBJECT, WWKSWKotlinx_serialization_coreStructureKindMAP, WWKSWKotlinx_serialization_coreStructureKindLIST, WWKSWKotlinx_serialization_coreStructureKindCLASS, WWKSWKotlinx_serialization_coreStructureKind, WWKSWKotlinx_serialization_coreSerializersModule, WWKSWKotlinx_serialization_coreSerialKindENUM, WWKSWKotlinx_serialization_coreSerialKindCONTEXTUAL, WWKSWKotlinx_serialization_coreSerialKind, WWKSWKotlinx_serialization_corePrimitiveKindSTRING, WWKSWKotlinx_serialization_corePrimitiveKindSHORT, WWKSWKotlinx_serialization_corePrimitiveKindLONG, WWKSWKotlinx_serialization_corePrimitiveKindINT, WWKSWKotlinx_serialization_corePrimitiveKindFLOAT, WWKSWKotlinx_serialization_corePrimitiveKindDOUBLE, WWKSWKotlinx_serialization_corePrimitiveKindCHAR, WWKSWKotlinx_serialization_corePrimitiveKindBYTE, WWKSWKotlinx_serialization_corePrimitiveKindBOOLEAN, WWKSWKotlinx_serialization_corePrimitiveKind, WWKSWKotlinx_serialization_corePolymorphicKindSEALED, WWKSWKotlinx_serialization_corePolymorphicKindOPEN, WWKSWKotlinx_serialization_corePolymorphicKind, WWKSWKotlinThrowable, WWKSWKotlinShortIterator, WWKSWKotlinShortArray, WWKSWKotlinRuntimeException, WWKSWKotlinNothing, WWKSWKotlinIntIterator, WWKSWKotlinIntArray, WWKSWKotlinIllegalStateException, WWKSWKotlinFloatIterator, WWKSWKotlinFloatArray, WWKSWKotlinException, WWKSWKotlinError, WWKSWKotlinEnumCompanion, WWKSWKotlinEnum<E>, WWKSWKotlinCancellationException, WWKSWKotlinByteIterator, WWKSWKotlinByteArray, WWKSWKotlinArray<T>, WWKSWInvalidQueryException, WWKSWInvalidCoordinatesException, WWKSWInt, WWKSWGooglePlacesSearchProviderKt, WWKSWGooglePlacesSearch, WWKSWGooglePlacesConfig, WWKSWFloat, WWKSWEastingNorthing, WWKSWDouble, WWKSWCoordinatesSearchProviderKt, WWKSWCoordinatesSearchConfig, WWKSWCoordinatesSearch, WWKSWByte, WWKSWBritishNationalGridSearchProviderKt, WWKSWBritishNationalGridSearchConfig, WWKSWBritishNationalGridSearch, WWKSWBoolean, WWKSWBase, WWKSWAutosuggestSearchConfig, WWKSWAudioSignalAmplitudeProcessor, UIImage, NSString, NSSet<ObjectType>, NSObject, NSNumber, NSMutableSet<ObjectType>, NSMutableDictionary<KeyType, ObjectType>, NSMutableArray<ObjectType>, NSError, NSDictionary<KeyType, ObjectType>, NSArray<ObjectType>;

@protocol WWKSWW3WVoiceDataSource, WWKSWW3WTextDataSource, WWKSWW3WLanguageSupportImageDataSource, WWKSWW3WLanguage, WWKSWW3WImageDataSource, WWKSWW3WAudioStreamEventsListener, WWKSWW3WAudioStreamEncoding, WWKSWUi_graphicsImageBitmap, WWKSWSkie_DispatcherDelegate, WWKSWSearchProvider, WWKSWResolvableSearchProvider, WWKSWKotlinx_serialization_coreSerializersModuleCollector, WWKSWKotlinx_serialization_coreSerializationStrategy, WWKSWKotlinx_serialization_coreSerialDescriptor, WWKSWKotlinx_serialization_coreKSerializer, WWKSWKotlinx_serialization_coreEncoder, WWKSWKotlinx_serialization_coreDeserializationStrategy, WWKSWKotlinx_serialization_coreDecoder, WWKSWKotlinx_serialization_coreCompositeEncoder, WWKSWKotlinx_serialization_coreCompositeDecoder, WWKSWKotlinx_coroutines_coreStateFlow, WWKSWKotlinx_coroutines_coreSharedFlow, WWKSWKotlinx_coroutines_coreRunnable, WWKSWKotlinx_coroutines_coreMutableStateFlow, WWKSWKotlinx_coroutines_coreMutableSharedFlow, WWKSWKotlinx_coroutines_coreFlowCollector, WWKSWKotlinx_coroutines_coreFlow, WWKSWKotlinKDeclarationContainer, WWKSWKotlinKClassifier, WWKSWKotlinKClass, WWKSWKotlinKAnnotatedElement, WWKSWKotlinIterator, WWKSWKotlinComparable, WWKSWKotlinAnnotation, NSCopying;

// Due to an Obj-C/Swift interop limitation, SKIE cannot generate Swift types with a lambda type argument.
// Example of such type is: A<() -> Unit> where A<T> is a generic class.
// To avoid compilation errors SKIE replaces these type arguments with __SkieLambdaErrorType, resulting in A<__SkieLambdaErrorType>.
// Generated declarations that reference __SkieLambdaErrorType cannot be called in any way and the __SkieLambdaErrorType class cannot be used.
// The original declarations can still be used in the same way as other declarations hidden by SKIE (and with the same limitations as without SKIE).
@interface __SkieLambdaErrorType : NSObject
- (instancetype _Nonnull)init __attribute__((unavailable));
+ (instancetype _Nonnull)new __attribute__((unavailable));
@end

// Due to an Obj-C/Swift interop limitation, SKIE cannot generate Swift code that uses external Obj-C types for which SKIE doesn't know a fully qualified name.
// This problem occurs when custom Cinterop bindings are used because those do not contain the name of the Framework that provides implementation for those binding.
// The name can be configured manually using the SKIE Gradle configuration key 'ClassInterop.CInteropFrameworkName' in the same way as other SKIE features.
// To avoid compilation errors SKIE replaces types with unknown Framework name with __SkieUnknownCInteropFrameworkErrorType.
// Generated declarations that reference __SkieUnknownCInteropFrameworkErrorType cannot be called in any way and the __SkieUnknownCInteropFrameworkErrorType class cannot be used.
@interface __SkieUnknownCInteropFrameworkErrorType : NSObject
- (instancetype _Nonnull)init __attribute__((unavailable));
+ (instancetype _Nonnull)new __attribute__((unavailable));
@end


NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface WWKSWBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface WWKSWBase (WWKSWBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface WWKSWMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface WWKSWMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorWWKSWKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface WWKSWNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end

__attribute__((swift_name("KotlinByte")))
@interface WWKSWByte : WWKSWNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface WWKSWUByte : WWKSWNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface WWKSWShort : WWKSWNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface WWKSWUShort : WWKSWNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface WWKSWInt : WWKSWNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface WWKSWUInt : WWKSWNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface WWKSWLong : WWKSWNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface WWKSWULong : WWKSWNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface WWKSWFloat : WWKSWNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface WWKSWDouble : WWKSWNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface WWKSWBoolean : WWKSWNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieColdFlowIterator")))
@interface WWKSWSkieColdFlowIterator<E> : WWKSWBase
- (instancetype)initWithFlow:(id<WWKSWKotlinx_coroutines_coreFlow>)flow __attribute__((swift_name("init(flow:)"))) __attribute__((objc_designated_initializer));
- (void)cancel __attribute__((swift_name("cancel()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)hasNextWithCompletionHandler:(void (^)(WWKSWBoolean * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("hasNext(completionHandler:)")));
- (E _Nullable)next __attribute__((swift_name("next()")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlow")))
@protocol WWKSWKotlinx_coroutines_coreFlow
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinFlow")))
@interface WWKSWSkieKotlinFlow<__covariant T> : WWKSWBase <WWKSWKotlinx_coroutines_coreFlow>
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreSharedFlow")))
@protocol WWKSWKotlinx_coroutines_coreSharedFlow <WWKSWKotlinx_coroutines_coreFlow>
@required
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlowCollector")))
@protocol WWKSWKotlinx_coroutines_coreFlowCollector
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(id _Nullable)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreMutableSharedFlow")))
@protocol WWKSWKotlinx_coroutines_coreMutableSharedFlow <WWKSWKotlinx_coroutines_coreSharedFlow, WWKSWKotlinx_coroutines_coreFlowCollector>
@required

/**
 * @note annotations
 *   kotlinx.coroutines.ExperimentalCoroutinesApi
*/
- (void)resetReplayCache __attribute__((swift_name("resetReplayCache()")));
- (BOOL)tryEmitValue:(id _Nullable)value __attribute__((swift_name("tryEmit(value:)")));
@property (readonly) id<WWKSWKotlinx_coroutines_coreStateFlow> subscriptionCount __attribute__((swift_name("subscriptionCount")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinMutableSharedFlow")))
@interface WWKSWSkieKotlinMutableSharedFlow<T> : WWKSWBase <WWKSWKotlinx_coroutines_coreMutableSharedFlow>
@property (readonly) NSArray<T> *replayCache __attribute__((swift_name("replayCache")));
@property (readonly) id<WWKSWKotlinx_coroutines_coreStateFlow> subscriptionCount __attribute__((swift_name("subscriptionCount")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreMutableSharedFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(T)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));

/**
 * @note annotations
 *   kotlinx.coroutines.ExperimentalCoroutinesApi
*/
- (void)resetReplayCache __attribute__((swift_name("resetReplayCache()")));
- (BOOL)tryEmitValue:(T)value __attribute__((swift_name("tryEmit(value:)")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreStateFlow")))
@protocol WWKSWKotlinx_coroutines_coreStateFlow <WWKSWKotlinx_coroutines_coreSharedFlow>
@required
@property (readonly) id _Nullable value __attribute__((swift_name("value")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreMutableStateFlow")))
@protocol WWKSWKotlinx_coroutines_coreMutableStateFlow <WWKSWKotlinx_coroutines_coreStateFlow, WWKSWKotlinx_coroutines_coreMutableSharedFlow>
@required
- (void)setValue:(id _Nullable)value __attribute__((swift_name("setValue(_:)")));
- (BOOL)compareAndSetExpect:(id _Nullable)expect update:(id _Nullable)update __attribute__((swift_name("compareAndSet(expect:update:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinMutableStateFlow")))
@interface WWKSWSkieKotlinMutableStateFlow<T> : WWKSWBase <WWKSWKotlinx_coroutines_coreMutableStateFlow>
@property (readonly) NSArray<T> *replayCache __attribute__((swift_name("replayCache")));
@property (readonly) id<WWKSWKotlinx_coroutines_coreStateFlow> subscriptionCount __attribute__((swift_name("subscriptionCount")));
@property T value __attribute__((swift_name("value")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreMutableStateFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
- (BOOL)compareAndSetExpect:(T)expect update:(T)update __attribute__((swift_name("compareAndSet(expect:update:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(T)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));

/**
 * @note annotations
 *   kotlinx.coroutines.ExperimentalCoroutinesApi
*/
- (void)resetReplayCache __attribute__((swift_name("resetReplayCache()")));
- (BOOL)tryEmitValue:(T)value __attribute__((swift_name("tryEmit(value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinOptionalFlow")))
@interface WWKSWSkieKotlinOptionalFlow<__covariant T> : WWKSWBase <WWKSWKotlinx_coroutines_coreFlow>
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinOptionalMutableSharedFlow")))
@interface WWKSWSkieKotlinOptionalMutableSharedFlow<T> : WWKSWBase <WWKSWKotlinx_coroutines_coreMutableSharedFlow>
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
@property (readonly) id<WWKSWKotlinx_coroutines_coreStateFlow> subscriptionCount __attribute__((swift_name("subscriptionCount")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreMutableSharedFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(T _Nullable)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));

/**
 * @note annotations
 *   kotlinx.coroutines.ExperimentalCoroutinesApi
*/
- (void)resetReplayCache __attribute__((swift_name("resetReplayCache()")));
- (BOOL)tryEmitValue:(T _Nullable)value __attribute__((swift_name("tryEmit(value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinOptionalMutableStateFlow")))
@interface WWKSWSkieKotlinOptionalMutableStateFlow<T> : WWKSWBase <WWKSWKotlinx_coroutines_coreMutableStateFlow>
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
@property (readonly) id<WWKSWKotlinx_coroutines_coreStateFlow> subscriptionCount __attribute__((swift_name("subscriptionCount")));
@property T _Nullable value __attribute__((swift_name("value")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreMutableStateFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
- (BOOL)compareAndSetExpect:(T _Nullable)expect update:(T _Nullable)update __attribute__((swift_name("compareAndSet(expect:update:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(T _Nullable)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));

/**
 * @note annotations
 *   kotlinx.coroutines.ExperimentalCoroutinesApi
*/
- (void)resetReplayCache __attribute__((swift_name("resetReplayCache()")));
- (BOOL)tryEmitValue:(T _Nullable)value __attribute__((swift_name("tryEmit(value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinOptionalSharedFlow")))
@interface WWKSWSkieKotlinOptionalSharedFlow<__covariant T> : WWKSWBase <WWKSWKotlinx_coroutines_coreSharedFlow>
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreSharedFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinOptionalStateFlow")))
@interface WWKSWSkieKotlinOptionalStateFlow<__covariant T> : WWKSWBase <WWKSWKotlinx_coroutines_coreStateFlow>
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
@property (readonly) T _Nullable value __attribute__((swift_name("value")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreStateFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinSharedFlow")))
@interface WWKSWSkieKotlinSharedFlow<__covariant T> : WWKSWBase <WWKSWKotlinx_coroutines_coreSharedFlow>
@property (readonly) NSArray<T> *replayCache __attribute__((swift_name("replayCache")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreSharedFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkieKotlinStateFlow")))
@interface WWKSWSkieKotlinStateFlow<__covariant T> : WWKSWBase <WWKSWKotlinx_coroutines_coreStateFlow>
@property (readonly) NSArray<T> *replayCache __attribute__((swift_name("replayCache")));
@property (readonly) T value __attribute__((swift_name("value")));
- (instancetype)initWithDelegate:(id<WWKSWKotlinx_coroutines_coreStateFlow>)delegate __attribute__((swift_name("init(_:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Skie_CancellationHandler")))
@interface WWKSWSkie_CancellationHandler : WWKSWBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)cancel __attribute__((swift_name("cancel()")));
@end

__attribute__((swift_name("Skie_DispatcherDelegate")))
@protocol WWKSWSkie_DispatcherDelegate
@required
- (void)dispatchBlock:(id<WWKSWKotlinx_coroutines_coreRunnable>)block __attribute__((swift_name("dispatch(block:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Skie_SuspendHandler")))
@interface WWKSWSkie_SuspendHandler : WWKSWBase
- (instancetype)initWithCancellationHandler:(WWKSWSkie_CancellationHandler *)cancellationHandler dispatcherDelegate:(id<WWKSWSkie_DispatcherDelegate>)dispatcherDelegate onResult:(void (^)(WWKSWSkie_SuspendResult *))onResult __attribute__((swift_name("init(cancellationHandler:dispatcherDelegate:onResult:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("Skie_SuspendResult")))
@interface WWKSWSkie_SuspendResult : WWKSWBase
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Skie_SuspendResult.Canceled")))
@interface WWKSWSkie_SuspendResultCanceled : WWKSWSkie_SuspendResult
@property (class, readonly, getter=shared) WWKSWSkie_SuspendResultCanceled *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)canceled __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Skie_SuspendResult.Error")))
@interface WWKSWSkie_SuspendResultError : WWKSWSkie_SuspendResult
@property (readonly) NSError *error __attribute__((swift_name("error")));
- (instancetype)initWithError:(NSError *)error __attribute__((swift_name("init(error:)"))) __attribute__((objc_designated_initializer));
- (WWKSWSkie_SuspendResultError *)doCopyError:(NSError *)error __attribute__((swift_name("doCopy(error:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Skie_SuspendResult.Success")))
@interface WWKSWSkie_SuspendResultSuccess : WWKSWSkie_SuspendResult
@property (readonly) id _Nullable value __attribute__((swift_name("value")));
- (instancetype)initWithValue:(id _Nullable)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (WWKSWSkie_SuspendResultSuccess *)doCopyValue:(id _Nullable)value __attribute__((swift_name("doCopy(value:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("W3WImageDataSource")))
@protocol WWKSWW3WImageDataSource
@required
- (void)scanImage:(WWKSWW3WImage *)image onScanning:(void (^)(void))onScanning onDetected:(void (^)(NSArray<NSString *> *))onDetected onError:(void (^)(WWKSWW3WError *))onError onCompleted:(void (^)(void))onCompleted __attribute__((swift_name("scan(image:onScanning:onDetected:onError:onCompleted:)")));
- (void)startOnReady:(void (^)(void))onReady onError:(void (^)(WWKSWW3WError *))onError __attribute__((swift_name("start(onReady:onError:)")));
- (void)stop __attribute__((swift_name("stop()")));
@end

__attribute__((swift_name("W3WLanguageSupportImageDataSource")))
@protocol WWKSWW3WLanguageSupportImageDataSource <WWKSWW3WImageDataSource>
@required
- (NSArray<id<WWKSWW3WLanguage>> *)availableLanguages __attribute__((swift_name("availableLanguages()")));
- (void)setLanguageLanguage:(id<WWKSWW3WLanguage>)language __attribute__((swift_name("setLanguage(language:)")));
- (BOOL)supportsLanguageLanguage:(id<WWKSWW3WLanguage>)language __attribute__((swift_name("supportsLanguage(language:)")));
@end

__attribute__((swift_name("W3WTextDataSource")))
@protocol WWKSWW3WTextDataSource
@required
- (WWKSWW3WResult<NSArray<WWKSWW3WSuggestion *> *> *)autosuggestInput:(NSString *)input options:(WWKSWW3WAutosuggestOptions * _Nullable)options __attribute__((swift_name("autosuggest(input:options:)")));
- (WWKSWW3WResult<NSSet<WWKSWW3WProprietaryLanguage *> *> *)availableLanguages __attribute__((swift_name("availableLanguages()")));
- (WWKSWW3WResult<WWKSWW3WAddress *> *)convertTo3waCoordinates:(WWKSWW3WCoordinates *)coordinates language:(id<WWKSWW3WLanguage>)language __attribute__((swift_name("convertTo3wa(coordinates:language:)")));
- (WWKSWW3WResult<WWKSWW3WAddress *> *)convertToCoordinatesWords:(NSString *)words __attribute__((swift_name("convertToCoordinates(words:)")));
- (WWKSWW3WResult<WWKSWW3WGridSection *> *)gridSectionBoundingBox:(WWKSWW3WRectangle *)boundingBox __attribute__((swift_name("gridSection(boundingBox:)")));
- (WWKSWW3WResult<WWKSWBoolean *> *)isValid3waWords:(NSString *)words __attribute__((swift_name("isValid3wa(words:)")));
- (NSString * _Nullable)versionVersion:(WWKSWW3WTextDataSourceVersion *)version __attribute__((swift_name("version(version:)")));
@end

__attribute__((swift_name("KotlinComparable")))
@protocol WWKSWKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface WWKSWKotlinEnum<E> : WWKSWBase <WWKSWKotlinComparable>
@property (class, readonly, getter=companion) WWKSWKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WTextDataSourceVersion")))
@interface WWKSWW3WTextDataSourceVersion : WWKSWKotlinEnum<WWKSWW3WTextDataSourceVersion *>
@property (class, readonly) WWKSWW3WTextDataSourceVersion *library __attribute__((swift_name("library")));
@property (class, readonly) WWKSWW3WTextDataSourceVersion *datasource __attribute__((swift_name("datasource")));
@property (class, readonly) WWKSWW3WTextDataSourceVersion *data __attribute__((swift_name("data")));
@property (class, readonly) NSArray<WWKSWW3WTextDataSourceVersion *> *entries __attribute__((swift_name("entries")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (WWKSWKotlinArray<WWKSWW3WTextDataSourceVersion *> *)values __attribute__((swift_name("values()")));
@end

__attribute__((swift_name("W3WVoiceDataSource")))
@protocol WWKSWW3WVoiceDataSource
@required
- (void)autosuggestInput:(WWKSWW3WAudioStream *)input voiceLanguage:(WWKSWW3WRFC5646Language *)voiceLanguage options:(WWKSWW3WAutosuggestOptions * _Nullable)options onRawResult:(void (^ _Nullable)(NSString *))onRawResult onResult:(void (^)(WWKSWW3WResult<NSArray<WWKSWW3WSuggestion *> *> *))onResult __attribute__((swift_name("autosuggest(input:voiceLanguage:options:onRawResult:onResult:)")));
- (NSSet<WWKSWW3WRFC5646Language *> *)availableLanguages __attribute__((swift_name("availableLanguages()")));
- (void)terminate __attribute__((swift_name("terminate()")));
- (NSString * _Nullable)versionVersion_:(WWKSWW3WVoiceDataSourceVersion *)version __attribute__((swift_name("version(version_:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WVoiceDataSourceVersion")))
@interface WWKSWW3WVoiceDataSourceVersion : WWKSWKotlinEnum<WWKSWW3WVoiceDataSourceVersion *>
@property (class, readonly) WWKSWW3WVoiceDataSourceVersion *library __attribute__((swift_name("library")));
@property (class, readonly) WWKSWW3WVoiceDataSourceVersion *datasource __attribute__((swift_name("datasource")));
@property (class, readonly) NSArray<WWKSWW3WVoiceDataSourceVersion *> *entries __attribute__((swift_name("entries")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (WWKSWKotlinArray<WWKSWW3WVoiceDataSourceVersion *> *)values __attribute__((swift_name("values()")));
@end

__attribute__((swift_name("W3WAudioStream")))
@interface WWKSWW3WAudioStream : WWKSWBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (WWKSWW3WAudioStream *)setEventsListenerListener:(id<WWKSWW3WAudioStreamEventsListener>)listener __attribute__((swift_name("setEventsListener(listener:)")));
- (WWKSWW3WAudioStream *)updateConfigConfig:(WWKSWW3WAudioStreamConfig *)config __attribute__((swift_name("updateConfig(config:)")));
@end

__attribute__((swift_name("W3WAudioStreamEventsListener")))
@protocol WWKSWW3WAudioStreamEventsListener
@required
- (void)onAudioStreamStateChangeState:(WWKSWW3WAudioStreamState *)state __attribute__((swift_name("onAudioStreamStateChange(state:)")));
- (void)onErrorError:(WWKSWW3WError *)error __attribute__((swift_name("onError(error:)")));
- (void)onVolumeChangeVolume:(float)volume __attribute__((swift_name("onVolumeChange(volume:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAudioStreamConfig")))
@interface WWKSWW3WAudioStreamConfig : WWKSWBase
@property (readonly) id<WWKSWW3WAudioStreamEncoding> encoding __attribute__((swift_name("encoding")));
@property (readonly) int32_t sampleRateInHz __attribute__((swift_name("sampleRateInHz")));
@property (readonly) int32_t samplesPerChannel __attribute__((swift_name("samplesPerChannel")));
- (instancetype)initWithSampleRateInHz:(int32_t)sampleRateInHz samplesPerChannel:(int32_t)samplesPerChannel encoding:(id<WWKSWW3WAudioStreamEncoding>)encoding __attribute__((swift_name("init(sampleRateInHz:samplesPerChannel:encoding:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WAudioStreamConfig *)doCopySampleRateInHz:(int32_t)sampleRateInHz samplesPerChannel:(int32_t)samplesPerChannel encoding:(id<WWKSWW3WAudioStreamEncoding>)encoding __attribute__((swift_name("doCopy(sampleRateInHz:samplesPerChannel:encoding:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("W3WAudioStreamEncoding")))
@protocol WWKSWW3WAudioStreamEncoding
@required
@property (readonly) id value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAudioStreamProxy")))
@interface WWKSWW3WAudioStreamProxy : WWKSWBase
@property (readonly) WWKSWW3WAudioStreamConfig *config __attribute__((swift_name("config")));
- (instancetype)initWithStream:(WWKSWW3WAudioStream *)stream __attribute__((swift_name("init(stream:)"))) __attribute__((objc_designated_initializer));
- (void)closeAudioInputStream __attribute__((swift_name("closeAudioInputStream()")));
- (void)openAudioInputStreamOnAudioSignal:(void (^)(WWKSWInt *, WWKSWKotlinShortArray *))onAudioSignal forceOpen:(BOOL)forceOpen __attribute__((swift_name("openAudioInputStream(onAudioSignal:forceOpen:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAudioStreamState")))
@interface WWKSWW3WAudioStreamState : WWKSWKotlinEnum<WWKSWW3WAudioStreamState *>
@property (class, readonly) WWKSWW3WAudioStreamState *listening __attribute__((swift_name("listening")));
@property (class, readonly) WWKSWW3WAudioStreamState *stopped __attribute__((swift_name("stopped")));
@property (class, readonly) NSArray<WWKSWW3WAudioStreamState *> *entries __attribute__((swift_name("entries")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (WWKSWKotlinArray<WWKSWW3WAudioStreamState *> *)values __attribute__((swift_name("values()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WIOSAudioStreamEncoding")))
@interface WWKSWW3WIOSAudioStreamEncoding : WWKSWKotlinEnum<WWKSWW3WIOSAudioStreamEncoding *> <WWKSWW3WAudioStreamEncoding>
@property (class, readonly) WWKSWW3WIOSAudioStreamEncoding *pcmF32le __attribute__((swift_name("pcmF32le")));
@property (class, readonly) WWKSWW3WIOSAudioStreamEncoding *pcmS16le __attribute__((swift_name("pcmS16le")));
@property (class, readonly) NSArray<WWKSWW3WIOSAudioStreamEncoding *> *entries __attribute__((swift_name("entries")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (WWKSWKotlinArray<WWKSWW3WIOSAudioStreamEncoding *> *)values __attribute__((swift_name("values()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WMicrophone")))
@interface WWKSWW3WMicrophone : WWKSWW3WAudioStream
@property (class, readonly, getter=companion) WWKSWW3WMicrophoneCompanion *companion __attribute__((swift_name("companion")));
- (instancetype)initWithConfig:(WWKSWW3WAudioStreamConfig *)config __attribute__((swift_name("init(config:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (WWKSWW3WAudioStream *)setEventsListenerListener:(id<WWKSWW3WAudioStreamEventsListener>)listener __attribute__((swift_name("setEventsListener(listener:)")));
- (WWKSWW3WAudioStream *)updateConfigConfig:(WWKSWW3WAudioStreamConfig *)config __attribute__((swift_name("updateConfig(config:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WMicrophone.Companion")))
@interface WWKSWW3WMicrophoneCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WMicrophoneCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (WWKSWW3WAudioStreamConfig *)defaultConfig __attribute__((swift_name("defaultConfig()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AudioSignalAmplitudeProcessor")))
@interface WWKSWAudioSignalAmplitudeProcessor : WWKSWBase
@property (class, readonly, getter=shared) WWKSWAudioSignalAmplitudeProcessor *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)audioSignalAmplitudeProcessor __attribute__((swift_name("init()")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface WWKSWKotlinThrowable : WWKSWBase
@property (readonly) WWKSWKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (WWKSWKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinError")))
@interface WWKSWKotlinError : WWKSWKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("W3WError")))
@interface WWKSWW3WError : WWKSWKotlinError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("W3WResult")))
@interface WWKSWW3WResult<__covariant T> : WWKSWBase
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WResultFailure")))
@interface WWKSWW3WResultFailure<__covariant T> : WWKSWW3WResult<T>
@property (readonly) WWKSWW3WError *error __attribute__((swift_name("error")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (instancetype)initWithError:(WWKSWW3WError *)error message:(NSString * _Nullable)message __attribute__((swift_name("init(error:message:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WResultSuccess")))
@interface WWKSWW3WResultSuccess<__covariant T> : WWKSWW3WResult<T>
@property (readonly) T _Nullable value __attribute__((swift_name("value")));
- (instancetype)initWithValue:(T _Nullable)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAddress")))
@interface WWKSWW3WAddress : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WAddressCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) WWKSWW3WCoordinates * _Nullable center __attribute__((swift_name("center")));
@property (readonly) WWKSWW3WCountry *country __attribute__((swift_name("country")));
@property (readonly) id<WWKSWW3WLanguage> language __attribute__((swift_name("language")));
@property (readonly) NSString *nearestPlace __attribute__((swift_name("nearestPlace")));
@property (readonly) WWKSWW3WRectangle * _Nullable square __attribute__((swift_name("square")));
@property (readonly) NSString *words __attribute__((swift_name("words")));
- (instancetype)initWithWords:(NSString *)words center:(WWKSWW3WCoordinates * _Nullable)center square:(WWKSWW3WRectangle * _Nullable)square language:(id<WWKSWW3WLanguage>)language country:(WWKSWW3WCountry *)country nearestPlace:(NSString *)nearestPlace __attribute__((swift_name("init(words:center:square:language:country:nearestPlace:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WAddress *)doCopyWords:(NSString *)words center:(WWKSWW3WCoordinates * _Nullable)center square:(WWKSWW3WRectangle * _Nullable)square language:(id<WWKSWW3WLanguage>)language country:(WWKSWW3WCountry *)country nearestPlace:(NSString *)nearestPlace __attribute__((swift_name("doCopy(words:center:square:language:country:nearestPlace:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=com/what3words/core/types/language/internal/W3WLanguageSerializer))
*/
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAddress.Companion")))
@interface WWKSWW3WAddressCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WAddressCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WCountry")))
@interface WWKSWW3WCountry : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WCountryCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) NSString *twoLetterCode __attribute__((swift_name("twoLetterCode")));
- (instancetype)initWithTwoLetterCode:(NSString *)twoLetterCode __attribute__((swift_name("init(twoLetterCode:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WCountry *)doCopyTwoLetterCode:(NSString *)twoLetterCode __attribute__((swift_name("doCopy(twoLetterCode:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WCountry.Companion")))
@interface WWKSWW3WCountryCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WCountryCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WSuggestion")))
@interface WWKSWW3WSuggestion : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WSuggestionCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) WWKSWW3WDistance * _Nullable distanceToFocus __attribute__((swift_name("distanceToFocus")));
@property (readonly) int32_t rank __attribute__((swift_name("rank")));
@property (readonly) WWKSWW3WAddress *w3wAddress __attribute__((swift_name("w3wAddress")));
- (instancetype)initWithW3wAddress:(WWKSWW3WAddress *)w3wAddress rank:(int32_t)rank distanceToFocus:(WWKSWW3WDistance * _Nullable)distanceToFocus __attribute__((swift_name("init(w3wAddress:rank:distanceToFocus:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WSuggestion *)doCopyW3wAddress:(WWKSWW3WAddress *)w3wAddress rank:(int32_t)rank distanceToFocus:(WWKSWW3WDistance * _Nullable)distanceToFocus __attribute__((swift_name("doCopy(w3wAddress:rank:distanceToFocus:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WSuggestion.Companion")))
@interface WWKSWW3WSuggestionCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WSuggestionCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WCircle")))
@interface WWKSWW3WCircle : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WCircleCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) WWKSWW3WCoordinates *center __attribute__((swift_name("center")));
@property (readonly) WWKSWW3WDistance *radius __attribute__((swift_name("radius")));
- (instancetype)initWithCenter:(WWKSWW3WCoordinates *)center radius:(WWKSWW3WDistance *)radius __attribute__((swift_name("init(center:radius:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WCircle *)doCopyCenter:(WWKSWW3WCoordinates *)center radius:(WWKSWW3WDistance *)radius __attribute__((swift_name("doCopy(center:radius:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WCircle.Companion")))
@interface WWKSWW3WCircleCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WCircleCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WCoordinates")))
@interface WWKSWW3WCoordinates : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WCoordinatesCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) double lat __attribute__((swift_name("lat")));
@property (readonly) double lng __attribute__((swift_name("lng")));
- (instancetype)initWithLat:(double)lat lng:(double)lng __attribute__((swift_name("init(lat:lng:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WCoordinates *)doCopyLat:(double)lat lng:(double)lng __attribute__((swift_name("doCopy(lat:lng:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WCoordinates.Companion")))
@interface WWKSWW3WCoordinatesCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WCoordinatesCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WDistance")))
@interface WWKSWW3WDistance : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WDistanceCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) double distance __attribute__((swift_name("distance")));
- (instancetype)initWithDistance:(double)distance __attribute__((swift_name("init(distance:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WDistance *)doCopyDistance:(double)distance __attribute__((swift_name("doCopy(distance:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WDistance.Companion")))
@interface WWKSWW3WDistanceCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WDistanceCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WGridSection")))
@interface WWKSWW3WGridSection : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WGridSectionCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) NSArray<WWKSWW3WLine *> *lines __attribute__((swift_name("lines")));
- (instancetype)initWithLines:(NSArray<WWKSWW3WLine *> *)lines __attribute__((swift_name("init(lines:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WGridSection *)doCopyLines:(NSArray<WWKSWW3WLine *> *)lines __attribute__((swift_name("doCopy(lines:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WGridSection.Companion")))
@interface WWKSWW3WGridSectionCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WGridSectionCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WLine")))
@interface WWKSWW3WLine : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WLineCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) WWKSWW3WCoordinates *end __attribute__((swift_name("end")));
@property (readonly) WWKSWW3WCoordinates *start __attribute__((swift_name("start")));
- (instancetype)initWithStart:(WWKSWW3WCoordinates *)start end:(WWKSWW3WCoordinates *)end __attribute__((swift_name("init(start:end:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WLine *)doCopyStart:(WWKSWW3WCoordinates *)start end:(WWKSWW3WCoordinates *)end __attribute__((swift_name("doCopy(start:end:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WLine.Companion")))
@interface WWKSWW3WLineCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WLineCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WPolygon")))
@interface WWKSWW3WPolygon : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WPolygonCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) NSArray<WWKSWW3WCoordinates *> *points __attribute__((swift_name("points")));
- (instancetype)initWithPoints:(NSArray<WWKSWW3WCoordinates *> *)points __attribute__((swift_name("init(points:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WPolygon *)doCopyPoints:(NSArray<WWKSWW3WCoordinates *> *)points __attribute__((swift_name("doCopy(points:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WPolygon.Companion")))
@interface WWKSWW3WPolygonCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WPolygonCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WRectangle")))
@interface WWKSWW3WRectangle : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WRectangleCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) WWKSWW3WCoordinates *northeast __attribute__((swift_name("northeast")));
@property (readonly) WWKSWW3WCoordinates *southwest __attribute__((swift_name("southwest")));
- (instancetype)initWithSouthwest:(WWKSWW3WCoordinates *)southwest northeast:(WWKSWW3WCoordinates *)northeast __attribute__((swift_name("init(southwest:northeast:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WRectangle *)doCopySouthwest:(WWKSWW3WCoordinates *)southwest northeast:(WWKSWW3WCoordinates *)northeast __attribute__((swift_name("doCopy(southwest:northeast:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WRectangle.Companion")))
@interface WWKSWW3WRectangleCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WRectangleCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WImage")))
@interface WWKSWW3WImage : WWKSWBase
- (instancetype)initWithImage:(UIImage *)image __attribute__((swift_name("init(image:)"))) __attribute__((objc_designated_initializer));
- (WWKSWKotlinByteArray *)toByteArray __attribute__((swift_name("toByteArray()")));
- (id<WWKSWUi_graphicsImageBitmap>)toImageBitmap __attribute__((swift_name("toImageBitmap()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=com/what3words/core/types/language/internal/W3WLanguageSerializer))
*/
__attribute__((swift_name("W3WLanguage")))
@protocol WWKSWW3WLanguage
@required
@property (readonly) NSString *w3wCode __attribute__((swift_name("w3wCode")));
@property (readonly) NSString * _Nullable w3wLocale __attribute__((swift_name("w3wLocale")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WLanguageCompanion")))
@interface WWKSWW3WLanguageCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WLanguageCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(WWKSWKotlinArray<id<WWKSWKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WProprietaryLanguage")))
@interface WWKSWW3WProprietaryLanguage : WWKSWBase <WWKSWW3WLanguage>
@property (class, readonly, getter=companion) WWKSWW3WProprietaryLanguageCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) NSString *code __attribute__((swift_name("code")));
@property (readonly) NSString * _Nullable locale __attribute__((swift_name("locale")));
@property (readonly) NSString * _Nullable name __attribute__((swift_name("name")));
@property (readonly) NSString * _Nullable nativeName __attribute__((swift_name("nativeName")));
@property (readonly) NSString *w3wCode __attribute__((swift_name("w3wCode")));
@property (readonly) NSString * _Nullable w3wLocale __attribute__((swift_name("w3wLocale")));
- (instancetype)initWithCode:(NSString *)code locale:(NSString * _Nullable)locale name:(NSString * _Nullable)name nativeName:(NSString * _Nullable)nativeName __attribute__((swift_name("init(code:locale:name:nativeName:)"))) __attribute__((objc_designated_initializer));
- (WWKSWW3WProprietaryLanguage *)doCopyCode:(NSString *)code locale:(NSString * _Nullable)locale name:(NSString * _Nullable)name nativeName:(NSString * _Nullable)nativeName __attribute__((swift_name("doCopy(code:locale:name:nativeName:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WProprietaryLanguage.Companion")))
@interface WWKSWW3WProprietaryLanguageCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WProprietaryLanguageCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=com/what3words/core/types/language/internal/W3WLanguageSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WRFC5646Language")))
@interface WWKSWW3WRFC5646Language : WWKSWKotlinEnum<WWKSWW3WRFC5646Language *> <WWKSWW3WLanguage>
@property (class, readonly, getter=companion) WWKSWW3WRFC5646LanguageCompanion *companion __attribute__((swift_name("companion")));
@property (class, readonly) WWKSWW3WRFC5646Language *af __attribute__((swift_name("af")));
@property (class, readonly) WWKSWW3WRFC5646Language *am __attribute__((swift_name("am")));
@property (class, readonly) WWKSWW3WRFC5646Language *ar __attribute__((swift_name("ar")));
@property (class, readonly) WWKSWW3WRFC5646Language *bg __attribute__((swift_name("bg")));
@property (class, readonly) WWKSWW3WRFC5646Language *bn __attribute__((swift_name("bn")));
@property (class, readonly) WWKSWW3WRFC5646Language *bsLatn __attribute__((swift_name("bsLatn")));
@property (class, readonly) WWKSWW3WRFC5646Language *bsCyrl __attribute__((swift_name("bsCyrl")));
@property (class, readonly) WWKSWW3WRFC5646Language *ca __attribute__((swift_name("ca")));
@property (class, readonly) WWKSWW3WRFC5646Language *cs __attribute__((swift_name("cs")));
@property (class, readonly) WWKSWW3WRFC5646Language *cy __attribute__((swift_name("cy")));
@property (class, readonly) WWKSWW3WRFC5646Language *da __attribute__((swift_name("da")));
@property (class, readonly) WWKSWW3WRFC5646Language *de __attribute__((swift_name("de")));
@property (class, readonly) WWKSWW3WRFC5646Language *el __attribute__((swift_name("el")));
@property (class, readonly) WWKSWW3WRFC5646Language *enAu __attribute__((swift_name("enAu")));
@property (class, readonly) WWKSWW3WRFC5646Language *enCa __attribute__((swift_name("enCa")));
@property (class, readonly) WWKSWW3WRFC5646Language *enGb __attribute__((swift_name("enGb")));
@property (class, readonly) WWKSWW3WRFC5646Language *enIn __attribute__((swift_name("enIn")));
@property (class, readonly) WWKSWW3WRFC5646Language *enUs __attribute__((swift_name("enUs")));
@property (class, readonly) WWKSWW3WRFC5646Language *esEs __attribute__((swift_name("esEs")));
@property (class, readonly) WWKSWW3WRFC5646Language *esMx __attribute__((swift_name("esMx")));
@property (class, readonly) WWKSWW3WRFC5646Language *et __attribute__((swift_name("et")));
@property (class, readonly) WWKSWW3WRFC5646Language *fa __attribute__((swift_name("fa")));
@property (class, readonly) WWKSWW3WRFC5646Language *fi __attribute__((swift_name("fi")));
@property (class, readonly) WWKSWW3WRFC5646Language *frCa __attribute__((swift_name("frCa")));
@property (class, readonly) WWKSWW3WRFC5646Language *frFr __attribute__((swift_name("frFr")));
@property (class, readonly) WWKSWW3WRFC5646Language *gu __attribute__((swift_name("gu")));
@property (class, readonly) WWKSWW3WRFC5646Language *he __attribute__((swift_name("he")));
@property (class, readonly) WWKSWW3WRFC5646Language *hi __attribute__((swift_name("hi")));
@property (class, readonly) WWKSWW3WRFC5646Language *hr __attribute__((swift_name("hr")));
@property (class, readonly) WWKSWW3WRFC5646Language *hu __attribute__((swift_name("hu")));
@property (class, readonly) WWKSWW3WRFC5646Language *id __attribute__((swift_name("id")));
@property (class, readonly) WWKSWW3WRFC5646Language *it __attribute__((swift_name("it")));
@property (class, readonly) WWKSWW3WRFC5646Language *ja __attribute__((swift_name("ja")));
@property (class, readonly) WWKSWW3WRFC5646Language *kkCyrl __attribute__((swift_name("kkCyrl")));
@property (class, readonly) WWKSWW3WRFC5646Language *kkLatn __attribute__((swift_name("kkLatn")));
@property (class, readonly) WWKSWW3WRFC5646Language *km __attribute__((swift_name("km")));
@property (class, readonly) WWKSWW3WRFC5646Language *kn __attribute__((swift_name("kn")));
@property (class, readonly) WWKSWW3WRFC5646Language *ko __attribute__((swift_name("ko")));
@property (class, readonly) WWKSWW3WRFC5646Language *lo __attribute__((swift_name("lo")));
@property (class, readonly) WWKSWW3WRFC5646Language *ml __attribute__((swift_name("ml")));
@property (class, readonly) WWKSWW3WRFC5646Language *mnCyrl __attribute__((swift_name("mnCyrl")));
@property (class, readonly) WWKSWW3WRFC5646Language *mnLatn __attribute__((swift_name("mnLatn")));
@property (class, readonly) WWKSWW3WRFC5646Language *mr __attribute__((swift_name("mr")));
@property (class, readonly) WWKSWW3WRFC5646Language *ms __attribute__((swift_name("ms")));
@property (class, readonly) WWKSWW3WRFC5646Language *ne __attribute__((swift_name("ne")));
@property (class, readonly) WWKSWW3WRFC5646Language *nl __attribute__((swift_name("nl")));
@property (class, readonly) WWKSWW3WRFC5646Language *no __attribute__((swift_name("no")));
@property (class, readonly) WWKSWW3WRFC5646Language *or_ __attribute__((swift_name("or_")));
@property (class, readonly) WWKSWW3WRFC5646Language *pa __attribute__((swift_name("pa")));
@property (class, readonly) WWKSWW3WRFC5646Language *pl __attribute__((swift_name("pl")));
@property (class, readonly) WWKSWW3WRFC5646Language *ptBr __attribute__((swift_name("ptBr")));
@property (class, readonly) WWKSWW3WRFC5646Language *ptPt __attribute__((swift_name("ptPt")));
@property (class, readonly) WWKSWW3WRFC5646Language *ro __attribute__((swift_name("ro")));
@property (class, readonly) WWKSWW3WRFC5646Language *ru __attribute__((swift_name("ru")));
@property (class, readonly) WWKSWW3WRFC5646Language *si __attribute__((swift_name("si")));
@property (class, readonly) WWKSWW3WRFC5646Language *sk __attribute__((swift_name("sk")));
@property (class, readonly) WWKSWW3WRFC5646Language *sl __attribute__((swift_name("sl")));
@property (class, readonly) WWKSWW3WRFC5646Language *srLatnRs __attribute__((swift_name("srLatnRs")));
@property (class, readonly) WWKSWW3WRFC5646Language *srCyrlRs __attribute__((swift_name("srCyrlRs")));
@property (class, readonly) WWKSWW3WRFC5646Language *srLatnMe __attribute__((swift_name("srLatnMe")));
@property (class, readonly) WWKSWW3WRFC5646Language *srCyrlMe __attribute__((swift_name("srCyrlMe")));
@property (class, readonly) WWKSWW3WRFC5646Language *sv __attribute__((swift_name("sv")));
@property (class, readonly) WWKSWW3WRFC5646Language *sw __attribute__((swift_name("sw")));
@property (class, readonly) WWKSWW3WRFC5646Language *ta __attribute__((swift_name("ta")));
@property (class, readonly) WWKSWW3WRFC5646Language *te __attribute__((swift_name("te")));
@property (class, readonly) WWKSWW3WRFC5646Language *th __attribute__((swift_name("th")));
@property (class, readonly) WWKSWW3WRFC5646Language *tr __attribute__((swift_name("tr")));
@property (class, readonly) WWKSWW3WRFC5646Language *uk __attribute__((swift_name("uk")));
@property (class, readonly) WWKSWW3WRFC5646Language *ur __attribute__((swift_name("ur")));
@property (class, readonly) WWKSWW3WRFC5646Language *vi __attribute__((swift_name("vi")));
@property (class, readonly) WWKSWW3WRFC5646Language *xh __attribute__((swift_name("xh")));
@property (class, readonly) WWKSWW3WRFC5646Language *zhHans __attribute__((swift_name("zhHans")));
@property (class, readonly) WWKSWW3WRFC5646Language *zhHantHk __attribute__((swift_name("zhHantHk")));
@property (class, readonly) WWKSWW3WRFC5646Language *zhHantTw __attribute__((swift_name("zhHantTw")));
@property (class, readonly) WWKSWW3WRFC5646Language *zu __attribute__((swift_name("zu")));
@property (class, readonly) NSArray<WWKSWW3WRFC5646Language *> *entries __attribute__((swift_name("entries")));
@property (readonly) NSString *code __attribute__((swift_name("code")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (WWKSWKotlinArray<WWKSWW3WRFC5646Language *> *)values __attribute__((swift_name("values()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WRFC5646Language.Companion")))
@interface WWKSWW3WRFC5646LanguageCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WRFC5646LanguageCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(WWKSWKotlinArray<id<WWKSWKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAutosuggestInputType")))
@interface WWKSWW3WAutosuggestInputType : WWKSWKotlinEnum<WWKSWW3WAutosuggestInputType *>
@property (class, readonly) WWKSWW3WAutosuggestInputType *text __attribute__((swift_name("text")));
@property (class, readonly) WWKSWW3WAutosuggestInputType *voconHybrid __attribute__((swift_name("voconHybrid")));
@property (class, readonly) WWKSWW3WAutosuggestInputType *nmdpAsr __attribute__((swift_name("nmdpAsr")));
@property (class, readonly) WWKSWW3WAutosuggestInputType *genericVoice __attribute__((swift_name("genericVoice")));
@property (class, readonly) WWKSWW3WAutosuggestInputType *speechmatics __attribute__((swift_name("speechmatics")));
@property (class, readonly) NSArray<WWKSWW3WAutosuggestInputType *> *entries __attribute__((swift_name("entries")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (WWKSWKotlinArray<WWKSWW3WAutosuggestInputType *> *)values __attribute__((swift_name("values()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAutosuggestOptions")))
@interface WWKSWW3WAutosuggestOptions : WWKSWBase
@property (class, readonly, getter=companion) WWKSWW3WAutosuggestOptionsCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) WWKSWW3WRectangle * _Nullable clipToBoundingBox __attribute__((swift_name("clipToBoundingBox")));
@property (readonly) WWKSWW3WCircle * _Nullable clipToCircle __attribute__((swift_name("clipToCircle")));
@property (readonly) NSArray<WWKSWW3WCountry *> *clipToCountry __attribute__((swift_name("clipToCountry")));
@property (readonly) WWKSWW3WPolygon * _Nullable clipToPolygon __attribute__((swift_name("clipToPolygon")));
@property (readonly) WWKSWW3WCoordinates * _Nullable focus __attribute__((swift_name("focus")));
@property (readonly) BOOL includeCoordinates __attribute__((swift_name("includeCoordinates")));
@property (readonly) WWKSWW3WAutosuggestInputType * _Nullable inputType __attribute__((swift_name("inputType")));
@property (readonly) id<WWKSWW3WLanguage> _Nullable language __attribute__((swift_name("language")));
@property (readonly) WWKSWInt * _Nullable nFocusResults __attribute__((swift_name("nFocusResults")));
@property (readonly) int32_t nResults __attribute__((swift_name("nResults")));
@property (readonly) BOOL preferLand __attribute__((swift_name("preferLand")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAutosuggestOptions.Builder")))
@interface WWKSWW3WAutosuggestOptionsBuilder : WWKSWBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (WWKSWW3WAutosuggestOptions *)build __attribute__((swift_name("build()")));
- (WWKSWW3WAutosuggestOptionsBuilder *)clipToBoundingBoxBoundingBox:(WWKSWW3WRectangle * _Nullable)boundingBox __attribute__((swift_name("clipToBoundingBox(boundingBox:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)clipToCircleCircle:(WWKSWW3WCircle * _Nullable)circle __attribute__((swift_name("clipToCircle(circle:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)clipToCountryCountry:(WWKSWKotlinArray<WWKSWW3WCountry *> *)country __attribute__((swift_name("clipToCountry(country:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)clipToPolygonPolygon:(WWKSWW3WPolygon *)polygon __attribute__((swift_name("clipToPolygon(polygon:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)focusFocus:(WWKSWW3WCoordinates * _Nullable)focus __attribute__((swift_name("focus(focus:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)fromOptionsOptions:(WWKSWW3WAutosuggestOptions *)options __attribute__((swift_name("fromOptions(options:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)includeCoordinatesInclude:(BOOL)include __attribute__((swift_name("includeCoordinates(include:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)inputTypeInputType:(WWKSWW3WAutosuggestInputType *)inputType __attribute__((swift_name("inputType(inputType:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)languageLanguage:(id<WWKSWW3WLanguage> _Nullable)language __attribute__((swift_name("language(language:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)nFocusResultsN:(int32_t)n __attribute__((swift_name("nFocusResults(n:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)nResultsN:(int32_t)n __attribute__((swift_name("nResults(n:)")));
- (WWKSWW3WAutosuggestOptionsBuilder *)preferLandPreferLand:(BOOL)preferLand __attribute__((swift_name("preferLand(preferLand:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WAutosuggestOptions.Companion")))
@interface WWKSWW3WAutosuggestOptionsCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWW3WAutosuggestOptionsCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("SearchPlugin")))
@interface WWKSWSearchPlugin<TConfig, TProvider> : WWKSWBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (TProvider)buildConfig:(TConfig)config textDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("build(config:textDataSource:)")));
@end

__attribute__((swift_name("SimpleSearchPlugin")))
@interface WWKSWSimpleSearchPlugin<TConfig, TProvider> : WWKSWSearchPlugin<TConfig, TProvider>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (TConfig)defaultConfig __attribute__((swift_name("defaultConfig()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BritishNationalGridSearch")))
@interface WWKSWBritishNationalGridSearch : WWKSWSimpleSearchPlugin<WWKSWBritishNationalGridSearchConfig *, id<WWKSWSearchProvider>>
@property (class, readonly, getter=shared) WWKSWBritishNationalGridSearch *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)britishNationalGridSearch __attribute__((swift_name("init()")));
- (id<WWKSWSearchProvider>)buildConfig:(WWKSWBritishNationalGridSearchConfig *)config textDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("build(config:textDataSource:)")));
- (WWKSWBritishNationalGridSearchConfig *)defaultConfig __attribute__((swift_name("defaultConfig()")));
@end

__attribute__((swift_name("SearchConfig")))
@interface WWKSWSearchConfig : WWKSWBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BritishNationalGridSearchConfig")))
@interface WWKSWBritishNationalGridSearchConfig : WWKSWSearchConfig
@property id<WWKSWW3WLanguage> language __attribute__((swift_name("language")));
- (instancetype)initWithLanguage:(id<WWKSWW3WLanguage>)language __attribute__((swift_name("init(language:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("EastingNorthing")))
@interface WWKSWEastingNorthing : WWKSWBase
@property (readonly) int32_t easting __attribute__((swift_name("easting")));
@property (readonly) int32_t northing __attribute__((swift_name("northing")));
- (instancetype)initWithEasting:(int32_t)easting northing:(int32_t)northing __attribute__((swift_name("init(easting:northing:)"))) __attribute__((objc_designated_initializer));
- (WWKSWEastingNorthing *)doCopyEasting:(int32_t)easting northing:(int32_t)northing __attribute__((swift_name("doCopy(easting:northing:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoordinatesSearch")))
@interface WWKSWCoordinatesSearch : WWKSWSimpleSearchPlugin<WWKSWCoordinatesSearchConfig *, id<WWKSWSearchProvider>>
@property (class, readonly, getter=shared) WWKSWCoordinatesSearch *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)coordinatesSearch __attribute__((swift_name("init()")));
- (id<WWKSWSearchProvider>)buildConfig:(WWKSWCoordinatesSearchConfig *)config textDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("build(config:textDataSource:)")));
- (WWKSWCoordinatesSearchConfig *)defaultConfig __attribute__((swift_name("defaultConfig()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoordinatesSearchConfig")))
@interface WWKSWCoordinatesSearchConfig : WWKSWSearchConfig
@property BOOL enableDDM __attribute__((swift_name("enableDDM")));
@property BOOL enableDMS __attribute__((swift_name("enableDMS")));
@property BOOL enableDecimal __attribute__((swift_name("enableDecimal")));
@property id<WWKSWW3WLanguage> language __attribute__((swift_name("language")));
- (instancetype)initWithEnableDecimal:(BOOL)enableDecimal enableDDM:(BOOL)enableDDM enableDMS:(BOOL)enableDMS language:(id<WWKSWW3WLanguage>)language __attribute__((swift_name("init(enableDecimal:enableDDM:enableDMS:language:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
@end

__attribute__((swift_name("SearchProvider")))
@protocol WWKSWSearchProvider
@required
- (BOOL)canHandleQuery:(NSString *)query __attribute__((swift_name("canHandle(query:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)searchQuery:(NSString *)query completionHandler:(void (^)(WWKSWW3WResult<NSArray<WWKSWSearchResult *> *> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("search(query:completionHandler:)")));
@property (readonly) NSString *providerId __attribute__((swift_name("providerId")));
@end

__attribute__((swift_name("ResolvableSearchProvider")))
@protocol WWKSWResolvableSearchProvider <WWKSWSearchProvider>
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)resolveData:(WWKSWSearchResultSearchSuggestion *)data completionHandler:(void (^)(WWKSWW3WResult<WWKSWSearchResultResolvedAddress *> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("resolve(data:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SearchPluginHandle")))
@interface WWKSWSearchPluginHandle : WWKSWBase
@end

__attribute__((swift_name("SearchResult")))
@interface WWKSWSearchResult : WWKSWBase
@property (class, readonly, getter=companion) WWKSWSearchResultCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) NSDictionary<NSString *, NSString *> *extras __attribute__((swift_name("extras")));
@property (readonly) NSString *providerId __attribute__((swift_name("providerId")));
@property (readonly) NSString *query __attribute__((swift_name("query")));
@property (readonly) NSString * _Nullable subtitle __attribute__((swift_name("subtitle")));
@property (readonly) NSString * _Nullable title __attribute__((swift_name("title")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SearchResult.Companion")))
@interface WWKSWSearchResultCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWSearchResultCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) NSString *EXTRAS_KEY_DISTANCE_TO_FOCUS __attribute__((swift_name("EXTRAS_KEY_DISTANCE_TO_FOCUS")));
@property (readonly) NSString *EXTRAS_KEY_RANK __attribute__((swift_name("EXTRAS_KEY_RANK")));
@property (readonly) NSString *EXTRAS_KEY_SUBTITLE __attribute__((swift_name("EXTRAS_KEY_SUBTITLE")));
@property (readonly) NSString *EXTRAS_KEY_SUGGESTED_ADDRESS __attribute__((swift_name("EXTRAS_KEY_SUGGESTED_ADDRESS")));
@property (readonly) NSString *EXTRAS_KEY_TITLE __attribute__((swift_name("EXTRAS_KEY_TITLE")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SearchResult.ResolvedAddress")))
@interface WWKSWSearchResultResolvedAddress : WWKSWSearchResult
@property (readonly) WWKSWW3WAddress *address __attribute__((swift_name("address")));
@property (readonly) NSDictionary<NSString *, NSString *> *extras __attribute__((swift_name("extras")));
@property (readonly) NSString *providerId __attribute__((swift_name("providerId")));
@property (readonly) NSString *query __attribute__((swift_name("query")));
- (instancetype)initWithQuery:(NSString *)query providerId:(NSString *)providerId address:(WWKSWW3WAddress *)address extras:(NSDictionary<NSString *, NSString *> *)extras __attribute__((swift_name("init(query:providerId:address:extras:)"))) __attribute__((objc_designated_initializer));
- (WWKSWSearchResultResolvedAddress *)doCopyQuery:(NSString *)query providerId:(NSString *)providerId address:(WWKSWW3WAddress *)address extras:(NSDictionary<NSString *, NSString *> *)extras __attribute__((swift_name("doCopy(query:providerId:address:extras:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SearchResult.SearchSuggestion")))
@interface WWKSWSearchResultSearchSuggestion : WWKSWSearchResult
@property (readonly) NSDictionary<NSString *, NSString *> *extras __attribute__((swift_name("extras")));
@property (readonly) NSString *providerId __attribute__((swift_name("providerId")));
@property (readonly) NSString *query __attribute__((swift_name("query")));
- (instancetype)initWithQuery:(NSString *)query providerId:(NSString *)providerId extras:(NSDictionary<NSString *, NSString *> *)extras __attribute__((swift_name("init(query:providerId:extras:)"))) __attribute__((objc_designated_initializer));
- (WWKSWSearchResultSearchSuggestion *)doCopyQuery:(NSString *)query providerId:(NSString *)providerId extras:(NSDictionary<NSString *, NSString *> *)extras __attribute__((swift_name("doCopy(query:providerId:extras:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString * _Nullable)suggestedAddressOrNull __attribute__((swift_name("suggestedAddressOrNull()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WSearchClient")))
@interface WWKSWW3WSearchClient : WWKSWBase
- (instancetype)initWithTextDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("init(textDataSource:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithTextDataSource:(id<WWKSWW3WTextDataSource>)textDataSource block:(void (^)(WWKSWW3WSearchClientConfig *))block __attribute__((swift_name("init(textDataSource:block:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of Exception to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)resolveData:(WWKSWSearchResultSearchSuggestion *)data completionHandler:(void (^)(WWKSWW3WResult<WWKSWSearchResultResolvedAddress *> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("resolve(data:completionHandler:)")));

/**
 * @note This method converts instances of Exception to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)searchQuery:(NSString *)query completionHandler:(void (^)(WWKSWW3WResult<NSArray<WWKSWSearchResult *> *> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("search(query:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("W3WSearchClient.Config")))
@interface WWKSWW3WSearchClientConfig : WWKSWBase
@property (readonly) NSArray<id<WWKSWSearchProvider>> *providers __attribute__((swift_name("providers")));
@property (readonly) id<WWKSWW3WTextDataSource> textDataSource __attribute__((swift_name("textDataSource")));
- (instancetype)initWithTextDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("init(textDataSource:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InvalidCoordinatesException")))
@interface WWKSWInvalidCoordinatesException : WWKSWW3WError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InvalidQueryException")))
@interface WWKSWInvalidQueryException : WWKSWW3WError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MissingSuggestionTitleException")))
@interface WWKSWMissingSuggestionTitleException : WWKSWW3WError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProviderNotFoundException")))
@interface WWKSWProviderNotFoundException : WWKSWW3WError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProviderNotResolvableException")))
@interface WWKSWProviderNotResolvableException : WWKSWW3WError
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("GooglePlacesConfig")))
@interface WWKSWGooglePlacesConfig : WWKSWSearchConfig
@property (readonly) NSString *apiKey __attribute__((swift_name("apiKey")));
@property (readonly) NSDictionary<NSString *, id> *headers __attribute__((swift_name("headers")));
@property (readonly) NSArray<NSString *> *includedRegionCodes __attribute__((swift_name("includedRegionCodes")));
@property (readonly) WWKSWW3WRFC5646Language *language __attribute__((swift_name("language")));
@property (readonly) WWKSWLocationBias * _Nullable locationBias __attribute__((swift_name("locationBias")));
@property (readonly) int32_t maxResults __attribute__((swift_name("maxResults")));
@property (readonly) int32_t minQueryLength __attribute__((swift_name("minQueryLength")));
@property (readonly) WWKSWW3WCoordinates * _Nullable origin __attribute__((swift_name("origin")));
@property (readonly) BOOL useSessionTokens __attribute__((swift_name("useSessionTokens")));
- (instancetype)initWithApiKey:(NSString *)apiKey language:(WWKSWW3WRFC5646Language *)language useSessionTokens:(BOOL)useSessionTokens minQueryLength:(int32_t)minQueryLength maxResults:(int32_t)maxResults locationBias:(WWKSWLocationBias * _Nullable)locationBias origin:(WWKSWW3WCoordinates * _Nullable)origin includedRegionCodes:(NSArray<NSString *> *)includedRegionCodes headers:(NSDictionary<NSString *, id> *)headers __attribute__((swift_name("init(apiKey:language:useSessionTokens:minQueryLength:maxResults:locationBias:origin:includedRegionCodes:headers:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (WWKSWGooglePlacesConfig *)doCopyApiKey:(NSString *)apiKey language:(WWKSWW3WRFC5646Language *)language useSessionTokens:(BOOL)useSessionTokens minQueryLength:(int32_t)minQueryLength maxResults:(int32_t)maxResults headers:(NSDictionary<NSString *, id> *)headers locationBias:(WWKSWLocationBias * _Nullable)locationBias origin:(WWKSWW3WCoordinates * _Nullable)origin includedRegionCodes:(NSArray<NSString *> *)includedRegionCodes __attribute__((swift_name("doCopy(apiKey:language:useSessionTokens:minQueryLength:maxResults:headers:locationBias:origin:includedRegionCodes:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("GooglePlacesSearch")))
@interface WWKSWGooglePlacesSearch : WWKSWSearchPlugin<WWKSWGooglePlacesConfig *, id<WWKSWResolvableSearchProvider>>
@property (class, readonly, getter=shared) WWKSWGooglePlacesSearch *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)googlePlacesSearch __attribute__((swift_name("init()")));
- (id<WWKSWResolvableSearchProvider>)buildConfig:(WWKSWGooglePlacesConfig *)config textDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("build(config:textDataSource:)")));
@end

__attribute__((swift_name("LocationBias")))
@interface WWKSWLocationBias : WWKSWBase
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LocationBias.Circle")))
@interface WWKSWLocationBiasCircle : WWKSWLocationBias
@property (readonly) WWKSWW3WCoordinates *center __attribute__((swift_name("center")));
@property (readonly) double radiusMeters __attribute__((swift_name("radiusMeters")));
- (instancetype)initWithCenter:(WWKSWW3WCoordinates *)center radiusMeters:(double)radiusMeters __attribute__((swift_name("init(center:radiusMeters:)"))) __attribute__((objc_designated_initializer));
- (WWKSWLocationBiasCircle *)doCopyCenter:(WWKSWW3WCoordinates *)center radiusMeters:(double)radiusMeters __attribute__((swift_name("doCopy(center:radiusMeters:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LocationBias.Rectangle")))
@interface WWKSWLocationBiasRectangle : WWKSWLocationBias
@property (readonly) WWKSWW3WCoordinates *high __attribute__((swift_name("high")));
@property (readonly) WWKSWW3WCoordinates *low __attribute__((swift_name("low")));
- (instancetype)initWithLow:(WWKSWW3WCoordinates *)low high:(WWKSWW3WCoordinates *)high __attribute__((swift_name("init(low:high:)"))) __attribute__((objc_designated_initializer));
- (WWKSWLocationBiasRectangle *)doCopyLow:(WWKSWW3WCoordinates *)low high:(WWKSWW3WCoordinates *)high __attribute__((swift_name("doCopy(low:high:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("AutosuggestSearchConfig")))
@interface WWKSWAutosuggestSearchConfig : WWKSWSearchConfig
@property (readonly) WWKSWW3WRectangle * _Nullable clipToBoundingBox __attribute__((swift_name("clipToBoundingBox")));
@property (readonly) WWKSWW3WCircle * _Nullable clipToCircle __attribute__((swift_name("clipToCircle")));
@property (readonly) WWKSWW3WPolygon * _Nullable clipToPolygon __attribute__((swift_name("clipToPolygon")));
@property (readonly) NSArray<WWKSWW3WCountry *> *clippedCountries __attribute__((swift_name("clippedCountries")));
@property (readonly) id<WWKSWW3WLanguage> _Nullable fallbackLanguage __attribute__((swift_name("fallbackLanguage")));
@property (readonly) WWKSWW3WCoordinates * _Nullable focus __attribute__((swift_name("focus")));
@property (readonly) BOOL includeCoordinates __attribute__((swift_name("includeCoordinates")));
@property (readonly) BOOL preferLand __attribute__((swift_name("preferLand")));
- (instancetype)initWithClippedCountries:(NSArray<WWKSWW3WCountry *> *)clippedCountries fallbackLanguage:(id<WWKSWW3WLanguage> _Nullable)fallbackLanguage preferLand:(BOOL)preferLand focus:(WWKSWW3WCoordinates * _Nullable)focus clipToCircle:(WWKSWW3WCircle * _Nullable)clipToCircle clipToBoundingBox:(WWKSWW3WRectangle * _Nullable)clipToBoundingBox clipToPolygon:(WWKSWW3WPolygon * _Nullable)clipToPolygon includeCoordinates:(BOOL)includeCoordinates __attribute__((swift_name("init(clippedCountries:fallbackLanguage:preferLand:focus:clipToCircle:clipToBoundingBox:clipToPolygon:includeCoordinates:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MayBeAThreeWordAddressSearch")))
@interface WWKSWMayBeAThreeWordAddressSearch : WWKSWSimpleSearchPlugin<WWKSWMayBeAThreeWordAddressSearchConfig *, id<WWKSWSearchProvider>>
@property (class, readonly, getter=shared) WWKSWMayBeAThreeWordAddressSearch *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)mayBeAThreeWordAddressSearch __attribute__((swift_name("init()")));
- (id<WWKSWSearchProvider>)buildConfig:(WWKSWMayBeAThreeWordAddressSearchConfig *)config textDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("build(config:textDataSource:)")));
- (WWKSWMayBeAThreeWordAddressSearchConfig *)defaultConfig __attribute__((swift_name("defaultConfig()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MayBeAThreeWordAddressSearchConfig")))
@interface WWKSWMayBeAThreeWordAddressSearchConfig : WWKSWAutosuggestSearchConfig
@property WWKSWW3WRectangle * _Nullable clipToBoundingBox __attribute__((swift_name("clipToBoundingBox")));
@property WWKSWW3WCircle * _Nullable clipToCircle __attribute__((swift_name("clipToCircle")));
@property WWKSWW3WPolygon * _Nullable clipToPolygon __attribute__((swift_name("clipToPolygon")));
@property NSArray<WWKSWW3WCountry *> *clippedCountries __attribute__((swift_name("clippedCountries")));
@property id<WWKSWW3WLanguage> _Nullable fallbackLanguage __attribute__((swift_name("fallbackLanguage")));
@property WWKSWW3WCoordinates * _Nullable focus __attribute__((swift_name("focus")));
@property BOOL includeCoordinates __attribute__((swift_name("includeCoordinates")));
@property BOOL preferLand __attribute__((swift_name("preferLand")));
- (instancetype)initWithClippedCountries:(NSArray<WWKSWW3WCountry *> *)clippedCountries fallbackLanguage:(id<WWKSWW3WLanguage> _Nullable)fallbackLanguage preferLand:(BOOL)preferLand focus:(WWKSWW3WCoordinates * _Nullable)focus clipToCircle:(WWKSWW3WCircle * _Nullable)clipToCircle clipToBoundingBox:(WWKSWW3WRectangle * _Nullable)clipToBoundingBox clipToPolygon:(WWKSWW3WPolygon * _Nullable)clipToPolygon includeCoordinates:(BOOL)includeCoordinates __attribute__((swift_name("init(clippedCountries:fallbackLanguage:preferLand:focus:clipToCircle:clipToBoundingBox:clipToPolygon:includeCoordinates:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ThreeWordAddressSearch")))
@interface WWKSWThreeWordAddressSearch : WWKSWSimpleSearchPlugin<WWKSWThreeWordAddressSearchConfig *, id<WWKSWSearchProvider>>
@property (class, readonly, getter=shared) WWKSWThreeWordAddressSearch *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)threeWordAddressSearch __attribute__((swift_name("init()")));
- (id<WWKSWSearchProvider>)buildConfig:(WWKSWThreeWordAddressSearchConfig *)config textDataSource:(id<WWKSWW3WTextDataSource>)textDataSource __attribute__((swift_name("build(config:textDataSource:)")));
- (WWKSWThreeWordAddressSearchConfig *)defaultConfig __attribute__((swift_name("defaultConfig()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ThreeWordAddressSearchConfig")))
@interface WWKSWThreeWordAddressSearchConfig : WWKSWAutosuggestSearchConfig
@property BOOL allowSpaceSeparator __attribute__((swift_name("allowSpaceSeparator")));
@property WWKSWW3WRectangle * _Nullable clipToBoundingBox __attribute__((swift_name("clipToBoundingBox")));
@property WWKSWW3WCircle * _Nullable clipToCircle __attribute__((swift_name("clipToCircle")));
@property WWKSWW3WPolygon * _Nullable clipToPolygon __attribute__((swift_name("clipToPolygon")));
@property NSArray<WWKSWW3WCountry *> *clippedCountries __attribute__((swift_name("clippedCountries")));
@property id<WWKSWW3WLanguage> _Nullable fallbackLanguage __attribute__((swift_name("fallbackLanguage")));
@property WWKSWW3WCoordinates * _Nullable focus __attribute__((swift_name("focus")));
@property BOOL includeCoordinates __attribute__((swift_name("includeCoordinates")));
@property int32_t maxResults __attribute__((swift_name("maxResults")));
@property BOOL preferLand __attribute__((swift_name("preferLand")));
- (instancetype)initWithClippedCountries:(NSArray<WWKSWW3WCountry *> *)clippedCountries fallbackLanguage:(id<WWKSWW3WLanguage> _Nullable)fallbackLanguage preferLand:(BOOL)preferLand focus:(WWKSWW3WCoordinates * _Nullable)focus clipToCircle:(WWKSWW3WCircle * _Nullable)clipToCircle clipToBoundingBox:(WWKSWW3WRectangle * _Nullable)clipToBoundingBox clipToPolygon:(WWKSWW3WPolygon * _Nullable)clipToPolygon includeCoordinates:(BOOL)includeCoordinates maxResults:(int32_t)maxResults allowSpaceSeparator:(BOOL)allowSpaceSeparator __attribute__((swift_name("init(clippedCountries:fallbackLanguage:preferLand:focus:clipToCircle:clipToBoundingBox:clipToPolygon:includeCoordinates:maxResults:allowSpaceSeparator:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithClippedCountries:(NSArray<WWKSWW3WCountry *> *)clippedCountries fallbackLanguage:(id<WWKSWW3WLanguage> _Nullable)fallbackLanguage preferLand:(BOOL)preferLand focus:(WWKSWW3WCoordinates * _Nullable)focus clipToCircle:(WWKSWW3WCircle * _Nullable)clipToCircle clipToBoundingBox:(WWKSWW3WRectangle * _Nullable)clipToBoundingBox clipToPolygon:(WWKSWW3WPolygon * _Nullable)clipToPolygon includeCoordinates:(BOOL)includeCoordinates __attribute__((swift_name("init(clippedCountries:fallbackLanguage:preferLand:focus:clipToCircle:clipToBoundingBox:clipToPolygon:includeCoordinates:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end

@interface WWKSWW3WAddress (Extensions)
- (NSString *)formattedWords __attribute__((swift_name("formattedWords()")));
@end

@interface WWKSWW3WCountry (Extensions)
- (BOOL)isLand __attribute__((swift_name("isLand()")));
@end

@interface WWKSWW3WDistance (Extensions)
- (double)km __attribute__((swift_name("km()")));
- (double)m __attribute__((swift_name("m()")));
@end

@interface WWKSWW3WGridSection (Extensions)
- (NSString *)toGeoJSON __attribute__((swift_name("toGeoJSON()")));
@end

@interface WWKSWW3WRFC5646Language (Extensions)
- (NSString *)getLanguageCode __attribute__((swift_name("getLanguageCode()")));
- (NSString * _Nullable)getRegionCode __attribute__((swift_name("getRegionCode()")));
- (NSString * _Nullable)getScriptCode __attribute__((swift_name("getScriptCode()")));
@end

@interface WWKSWSearchPlugin (Extensions)
- (WWKSWSearchPluginHandle *)asHandleConfig:(WWKSWSearchConfig *)config __attribute__((swift_name("asHandle(config:)")));
@end

@interface WWKSWSimpleSearchPlugin (Extensions)
- (WWKSWSearchPluginHandle *)asHandle __attribute__((swift_name("asHandle()")));
@end

@interface WWKSWW3WSearchClient (Extensions)
- (void)addPluginPlugin:(WWKSWSearchPluginHandle *)plugin priority:(int32_t)priority __attribute__((swift_name("addPlugin(plugin:priority:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BritishNationalGridSearchProviderKt")))
@interface WWKSWBritishNationalGridSearchProviderKt : WWKSWBase
@property (class, readonly) NSString *BRITISH_NATIONAL_GRID_PROVIDER_ID __attribute__((swift_name("BRITISH_NATIONAL_GRID_PROVIDER_ID")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CoordinatesSearchProviderKt")))
@interface WWKSWCoordinatesSearchProviderKt : WWKSWBase
@property (class, readonly) NSString *COORDINATES_PROVIDER_ID __attribute__((swift_name("COORDINATES_PROVIDER_ID")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("GooglePlacesSearchProviderKt")))
@interface WWKSWGooglePlacesSearchProviderKt : WWKSWBase
@property (class, readonly) NSString *GOOGLE_PLACES_PROVIDER_ID __attribute__((swift_name("GOOGLE_PLACES_PROVIDER_ID")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MayBeAThreeWordAddressSearchProviderKt")))
@interface WWKSWMayBeAThreeWordAddressSearchProviderKt : WWKSWBase
@property (class, readonly) NSString *MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID __attribute__((swift_name("MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ThreeWordAddressSearchProviderKt")))
@interface WWKSWThreeWordAddressSearchProviderKt : WWKSWBase
@property (class, readonly) NSString *THREE_WORD_ADDRESS_PROVIDER_ID __attribute__((swift_name("THREE_WORD_ADDRESS_PROVIDER_ID")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("__SkieSuspendWrappersKt")))
@interface WWKSW__SkieSuspendWrappersKt : WWKSWBase
+ (void)Skie_Suspend__0__hasNextDispatchReceiver:(WWKSWSkieColdFlowIterator<id> *)dispatchReceiver suspendHandler:(WWKSWSkie_SuspendHandler *)suspendHandler __attribute__((swift_name("Skie_Suspend__0__hasNext(dispatchReceiver:suspendHandler:)")));
+ (void)Skie_Suspend__1__collectDispatchReceiver:(id<WWKSWKotlinx_coroutines_coreFlow>)dispatchReceiver collector:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)collector suspendHandler:(WWKSWSkie_SuspendHandler *)suspendHandler __attribute__((swift_name("Skie_Suspend__1__collect(dispatchReceiver:collector:suspendHandler:)")));
+ (void)Skie_Suspend__2__emitDispatchReceiver:(id<WWKSWKotlinx_coroutines_coreFlowCollector>)dispatchReceiver value:(id _Nullable)value suspendHandler:(WWKSWSkie_SuspendHandler *)suspendHandler __attribute__((swift_name("Skie_Suspend__2__emit(dispatchReceiver:value:suspendHandler:)")));
+ (void)Skie_Suspend__3__executeSearchDispatchReceiver:(id<WWKSWSearchProvider>)dispatchReceiver query:(NSString *)query suspendHandler:(WWKSWSkie_SuspendHandler *)suspendHandler __attribute__((swift_name("Skie_Suspend__3__executeSearch(dispatchReceiver:query:suspendHandler:)")));
+ (void)Skie_Suspend__4__resolveDispatchReceiver:(id<WWKSWResolvableSearchProvider>)dispatchReceiver data:(WWKSWSearchResultSearchSuggestion *)data suspendHandler:(WWKSWSkie_SuspendHandler *)suspendHandler __attribute__((swift_name("Skie_Suspend__4__resolve(dispatchReceiver:data:suspendHandler:)")));
+ (void)Skie_Suspend__5__resolveDispatchReceiver:(WWKSWW3WSearchClient *)dispatchReceiver data:(WWKSWSearchResultSearchSuggestion *)data suspendHandler:(WWKSWSkie_SuspendHandler *)suspendHandler __attribute__((swift_name("Skie_Suspend__5__resolve(dispatchReceiver:data:suspendHandler:)")));
+ (void)Skie_Suspend__6__searchDispatchReceiver:(WWKSWW3WSearchClient *)dispatchReceiver query:(NSString *)query suspendHandler:(WWKSWSkie_SuspendHandler *)suspendHandler __attribute__((swift_name("Skie_Suspend__6__search(dispatchReceiver:query:suspendHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("__SkieTypeExportsKt")))
@interface WWKSW__SkieTypeExportsKt : WWKSWBase
+ (void)skieTypeExports_0P0:(WWKSWKotlinx_serialization_corePolymorphicKind *)p0 p1:(WWKSWKotlinx_serialization_corePolymorphicKindOPEN *)p1 p2:(WWKSWKotlinx_serialization_corePolymorphicKindSEALED *)p2 p3:(WWKSWKotlinx_serialization_corePrimitiveKind *)p3 p4:(WWKSWKotlinx_serialization_corePrimitiveKindBOOLEAN *)p4 p5:(WWKSWKotlinx_serialization_corePrimitiveKindBYTE *)p5 p6:(WWKSWKotlinx_serialization_corePrimitiveKindCHAR *)p6 p7:(WWKSWKotlinx_serialization_corePrimitiveKindDOUBLE *)p7 p8:(WWKSWKotlinx_serialization_corePrimitiveKindFLOAT *)p8 p9:(WWKSWKotlinx_serialization_corePrimitiveKindINT *)p9 p10:(WWKSWKotlinx_serialization_corePrimitiveKindLONG *)p10 p11:(WWKSWKotlinx_serialization_corePrimitiveKindSHORT *)p11 p12:(WWKSWKotlinx_serialization_corePrimitiveKindSTRING *)p12 p13:(WWKSWKotlinx_serialization_coreSerialKindCONTEXTUAL *)p13 p14:(WWKSWKotlinx_serialization_coreSerialKindENUM *)p14 p15:(WWKSWKotlinx_serialization_coreStructureKind *)p15 p16:(WWKSWKotlinx_serialization_coreStructureKindCLASS *)p16 p17:(WWKSWKotlinx_serialization_coreStructureKindLIST *)p17 p18:(WWKSWKotlinx_serialization_coreStructureKindMAP *)p18 p19:(WWKSWKotlinx_serialization_coreStructureKindOBJECT *)p19 __attribute__((swift_name("skieTypeExports_0(p0:p1:p2:p3:p4:p5:p6:p7:p8:p9:p10:p11:p12:p13:p14:p15:p16:p17:p18:p19:)")));
@end

__attribute__((swift_name("KotlinException")))
@interface WWKSWKotlinException : WWKSWKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface WWKSWKotlinRuntimeException : WWKSWKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalStateException")))
@interface WWKSWKotlinIllegalStateException : WWKSWKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.4")
*/
__attribute__((swift_name("KotlinCancellationException")))
@interface WWKSWKotlinCancellationException : WWKSWKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(WWKSWKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreRunnable")))
@protocol WWKSWKotlinx_coroutines_coreRunnable
@required
- (void)run __attribute__((swift_name("run()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface WWKSWKotlinEnumCompanion : WWKSWBase
@property (class, readonly, getter=shared) WWKSWKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface WWKSWKotlinArray<T> : WWKSWBase
@property (readonly) int32_t size __attribute__((swift_name("size")));
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(WWKSWInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<WWKSWKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinShortArray")))
@interface WWKSWKotlinShortArray : WWKSWBase
@property (readonly) int32_t size __attribute__((swift_name("size")));
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(WWKSWShort *(^)(WWKSWInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int16_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (WWKSWKotlinShortIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int16_t)value __attribute__((swift_name("set(index:value:)")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol WWKSWKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<WWKSWKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<WWKSWKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol WWKSWKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<WWKSWKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<WWKSWKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol WWKSWKotlinx_serialization_coreKSerializer <WWKSWKotlinx_serialization_coreSerializationStrategy, WWKSWKotlinx_serialization_coreDeserializationStrategy>
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface WWKSWKotlinByteArray : WWKSWBase
@property (readonly) int32_t size __attribute__((swift_name("size")));
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(WWKSWByte *(^)(WWKSWInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (WWKSWKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@end

__attribute__((swift_name("Ui_graphicsImageBitmap")))
@protocol WWKSWUi_graphicsImageBitmap
@required
- (void)prepareToDraw __attribute__((swift_name("prepareToDraw()")));
- (void)readPixelsBuffer:(WWKSWKotlinIntArray *)buffer startX:(int32_t)startX startY:(int32_t)startY width:(int32_t)width height:(int32_t)height bufferOffset:(int32_t)bufferOffset stride:(int32_t)stride __attribute__((swift_name("readPixels(buffer:startX:startY:width:height:bufferOffset:stride:)")));
@property (readonly) WWKSWUi_graphicsColorSpace *colorSpace __attribute__((swift_name("colorSpace")));
@property (readonly) int32_t config __attribute__((swift_name("config")));
@property (readonly) BOOL hasAlpha __attribute__((swift_name("hasAlpha")));
@property (readonly) int32_t height __attribute__((swift_name("height")));
@property (readonly) int32_t width __attribute__((swift_name("width")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface WWKSWKotlinx_serialization_coreSerialKind : WWKSWBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_corePolymorphicKind")))
@interface WWKSWKotlinx_serialization_corePolymorphicKind : WWKSWKotlinx_serialization_coreSerialKind
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePolymorphicKind.OPEN")))
@interface WWKSWKotlinx_serialization_corePolymorphicKindOPEN : WWKSWKotlinx_serialization_corePolymorphicKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePolymorphicKindOPEN *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)oPEN __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePolymorphicKind.SEALED")))
@interface WWKSWKotlinx_serialization_corePolymorphicKindSEALED : WWKSWKotlinx_serialization_corePolymorphicKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePolymorphicKindSEALED *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)sEALED __attribute__((swift_name("init()")));
@end

__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind")))
@interface WWKSWKotlinx_serialization_corePrimitiveKind : WWKSWKotlinx_serialization_coreSerialKind
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.BOOLEAN")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindBOOLEAN : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindBOOLEAN *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)bOOLEAN __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.BYTE")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindBYTE : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindBYTE *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)bYTE __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.CHAR")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindCHAR : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindCHAR *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)cHAR __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.DOUBLE")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindDOUBLE : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindDOUBLE *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)dOUBLE __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.FLOAT")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindFLOAT : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindFLOAT *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)fLOAT __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.INT")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindINT : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindINT *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)iNT __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.LONG")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindLONG : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindLONG *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)lONG __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.SHORT")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindSHORT : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindSHORT *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)sHORT __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_corePrimitiveKind.STRING")))
@interface WWKSWKotlinx_serialization_corePrimitiveKindSTRING : WWKSWKotlinx_serialization_corePrimitiveKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_corePrimitiveKindSTRING *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)sTRING __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind.CONTEXTUAL")))
@interface WWKSWKotlinx_serialization_coreSerialKindCONTEXTUAL : WWKSWKotlinx_serialization_coreSerialKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_coreSerialKindCONTEXTUAL *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)cONTEXTUAL __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind.ENUM")))
@interface WWKSWKotlinx_serialization_coreSerialKindENUM : WWKSWKotlinx_serialization_coreSerialKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_coreSerialKindENUM *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)eNUM __attribute__((swift_name("init()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreStructureKind")))
@interface WWKSWKotlinx_serialization_coreStructureKind : WWKSWKotlinx_serialization_coreSerialKind
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_coreStructureKind.CLASS")))
@interface WWKSWKotlinx_serialization_coreStructureKindCLASS : WWKSWKotlinx_serialization_coreStructureKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_coreStructureKindCLASS *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)cLASS __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_coreStructureKind.LIST")))
@interface WWKSWKotlinx_serialization_coreStructureKindLIST : WWKSWKotlinx_serialization_coreStructureKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_coreStructureKindLIST *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)lIST __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_coreStructureKind.MAP")))
@interface WWKSWKotlinx_serialization_coreStructureKindMAP : WWKSWKotlinx_serialization_coreStructureKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_coreStructureKindMAP *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)mAP __attribute__((swift_name("init()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_coreStructureKind.OBJECT")))
@interface WWKSWKotlinx_serialization_coreStructureKindOBJECT : WWKSWKotlinx_serialization_coreStructureKind
@property (class, readonly, getter=shared) WWKSWKotlinx_serialization_coreStructureKindOBJECT *shared __attribute__((swift_name("shared")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)oBJECT __attribute__((swift_name("init()")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol WWKSWKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end

__attribute__((swift_name("KotlinShortIterator")))
@interface WWKSWKotlinShortIterator : WWKSWBase <WWKSWKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (WWKSWShort *)next __attribute__((swift_name("next()")));
- (int16_t)nextShort __attribute__((swift_name("nextShort()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol WWKSWKotlinx_serialization_coreEncoder
@required
- (id<WWKSWKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<WWKSWKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<WWKSWKotlinx_serialization_coreEncoder>)encodeInlineDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("encodeInline(descriptor:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNull __attribute__((swift_name("encodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableValueSerializer:(id<WWKSWKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<WWKSWKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) WWKSWKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol WWKSWKotlinx_serialization_coreSerialDescriptor
@required
- (NSArray<id<WWKSWKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));
- (id<WWKSWKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));
@property (readonly) NSArray<id<WWKSWKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isInline __attribute__((swift_name("isInline")));
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));
@property (readonly) WWKSWKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol WWKSWKotlinx_serialization_coreDecoder
@required
- (id<WWKSWKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<WWKSWKotlinx_serialization_coreDecoder>)decodeInlineDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeInline(descriptor:)")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (WWKSWKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<WWKSWKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<WWKSWKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) WWKSWKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("KotlinByteIterator")))
@interface WWKSWKotlinByteIterator : WWKSWBase <WWKSWKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (WWKSWByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntArray")))
@interface WWKSWKotlinIntArray : WWKSWBase
@property (readonly) int32_t size __attribute__((swift_name("size")));
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(WWKSWInt *(^)(WWKSWInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int32_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (WWKSWKotlinIntIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int32_t)value __attribute__((swift_name("set(index:value:)")));
@end

__attribute__((swift_name("Ui_graphicsColorSpace")))
@interface WWKSWUi_graphicsColorSpace : WWKSWBase
@property (readonly) int32_t componentCount __attribute__((swift_name("componentCount")));
@property (readonly) BOOL isSrgb __attribute__((swift_name("isSrgb")));
@property (readonly) BOOL isWideGamut __attribute__((swift_name("isWideGamut")));
@property (readonly) int64_t model __attribute__((swift_name("model")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
- (instancetype)initWithName:(NSString *)name model:(int64_t)model __attribute__((swift_name("init(name:model:)"))) __attribute__((objc_designated_initializer));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/**
 * @note annotations
 *   androidx.annotation.Size(min=3.toLong())
 * @param v annotations androidx.annotation.Size(min=3.toLong())
*/
- (WWKSWKotlinFloatArray *)fromXyzV:(WWKSWKotlinFloatArray *)v __attribute__((swift_name("fromXyz(v:)")));

/**
 * @note annotations
 *   androidx.annotation.Size(min=3.toLong())
*/
- (WWKSWKotlinFloatArray *)fromXyzX:(float)x y:(float)y z:(float)z __attribute__((swift_name("fromXyz(x:y:z:)")));

/**
 * @param component annotations androidx.annotation.IntRange(from=0.toLong(), to=3.toLong())
*/
- (float)getMaxValueComponent:(int32_t)component __attribute__((swift_name("getMaxValue(component:)")));

/**
 * @param component annotations androidx.annotation.IntRange(from=0.toLong(), to=3.toLong())
*/
- (float)getMinValueComponent:(int32_t)component __attribute__((swift_name("getMinValue(component:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * @note annotations
 *   androidx.annotation.Size(min=3.toLong())
 * @param v annotations androidx.annotation.Size(min=3.toLong())
*/
- (WWKSWKotlinFloatArray *)toXyzV:(WWKSWKotlinFloatArray *)v __attribute__((swift_name("toXyz(v:)")));

/**
 * @note annotations
 *   androidx.annotation.Size(value=3.toLong())
*/
- (WWKSWKotlinFloatArray *)toXyzR:(float)r g:(float)g b:(float)b __attribute__((swift_name("toXyz(r:g:b:)")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol WWKSWKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<WWKSWKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<WWKSWKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<WWKSWKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) WWKSWKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface WWKSWKotlinx_serialization_coreSerializersModule : WWKSWBase

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)dumpToCollector:(id<WWKSWKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<WWKSWKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<WWKSWKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<WWKSWKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<WWKSWKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<WWKSWKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<WWKSWKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<WWKSWKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end

__attribute__((swift_name("KotlinAnnotation")))
@protocol WWKSWKotlinAnnotation
@required
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol WWKSWKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<WWKSWKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<WWKSWKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<WWKSWKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<WWKSWKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) WWKSWKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface WWKSWKotlinNothing : WWKSWBase
@end

__attribute__((swift_name("KotlinIntIterator")))
@interface WWKSWKotlinIntIterator : WWKSWBase <WWKSWKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (WWKSWInt *)next __attribute__((swift_name("next()")));
- (int32_t)nextInt __attribute__((swift_name("nextInt()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinFloatArray")))
@interface WWKSWKotlinFloatArray : WWKSWBase
@property (readonly) int32_t size __attribute__((swift_name("size")));
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(WWKSWFloat *(^)(WWKSWInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (float)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (WWKSWKotlinFloatIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(float)value __attribute__((swift_name("set(index:value:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol WWKSWKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<WWKSWKotlinKClass>)kClass provider:(id<WWKSWKotlinx_serialization_coreKSerializer> (^)(NSArray<id<WWKSWKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<WWKSWKotlinKClass>)kClass serializer:(id<WWKSWKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<WWKSWKotlinKClass>)baseClass actualClass:(id<WWKSWKotlinKClass>)actualClass actualSerializer:(id<WWKSWKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<WWKSWKotlinKClass>)baseClass defaultDeserializerProvider:(id<WWKSWKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultDeserializerProvider:)"))) __attribute__((deprecated("Deprecated in favor of function with more precise name: polymorphicDefaultDeserializer")));
- (void)polymorphicDefaultDeserializerBaseClass:(id<WWKSWKotlinKClass>)baseClass defaultDeserializerProvider:(id<WWKSWKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefaultDeserializer(baseClass:defaultDeserializerProvider:)")));
- (void)polymorphicDefaultSerializerBaseClass:(id<WWKSWKotlinKClass>)baseClass defaultSerializerProvider:(id<WWKSWKotlinx_serialization_coreSerializationStrategy> _Nullable (^)(id))defaultSerializerProvider __attribute__((swift_name("polymorphicDefaultSerializer(baseClass:defaultSerializerProvider:)")));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol WWKSWKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol WWKSWKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol WWKSWKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol WWKSWKotlinKClass <WWKSWKotlinKDeclarationContainer, WWKSWKotlinKAnnotatedElement, WWKSWKotlinKClassifier>
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end

__attribute__((swift_name("KotlinFloatIterator")))
@interface WWKSWKotlinFloatIterator : WWKSWBase <WWKSWKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (WWKSWFloat *)next __attribute__((swift_name("next()")));
- (float)nextFloat __attribute__((swift_name("nextFloat()")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
